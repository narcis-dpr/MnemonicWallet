package com.example.mnemonicwallet.data.repository

import com.example.mnemonicwallet.data.model.Source
import com.example.mnemonicwallet.data.model.W3JLCredential
import com.example.mnemonicwallet.data.model.W3JLWallet
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.novacrypto.bip39.MnemonicGenerator
import io.github.novacrypto.bip39.Words
import io.github.novacrypto.bip39.wordlists.English
import io.reactivex.Single
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.HDKeyDerivation
import org.bitcoinj.wallet.DeterministicSeed
import org.web3j.crypto.*
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.util.*
import kotlin.collections.List

class WalletService() : WalletRepository {

    private val RADIX = 16

    // Use to create Json data
    private val PARAM_N = 8192
    private val PARAM_P = 1
    private val objectMapper = jacksonObjectMapper()

    // HDPath use to generate wallet from mnemonic
    private val ETH_TYPE = "m/44'/60'/0'/0/0"

    override fun createMnemonics(entropy: ByteArray): String {
        val sb = StringBuilder()
        SecureRandom().nextBytes(entropy)
        MnemonicGenerator(English.INSTANCE)
            .createMnemonic(
                entropy,
            ) {
                sb.append(it)
            }
        return sb.toString()
    }

    override fun createMnemonics(): String {
        return createMnemonics(ByteArray(Words.TWELVE.byteLength()))
    }

    private fun createHDWalletFromMnemonic(mnemonics: List<String>): Single<W3JLWallet> {
        return Single.create { emitter ->
            try {
                val pathArray =
                    ETH_TYPE.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val passphrase = ""
                val creationTimeSeconds = System.currentTimeMillis() / 1000
                val ds = DeterministicSeed(mnemonics, null, passphrase, creationTimeSeconds)
                val seedBytes = ds.seedBytes
                var dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes)
                for (i in 1 until pathArray.size) {
                    val childNumber: ChildNumber
                    childNumber = if (pathArray[i].endsWith("'")) {
                        val number = Integer.parseInt(
                            pathArray[i].substring(
                                0,
                                pathArray[i].length - 1,
                            ),
                        )
                        ChildNumber(number, true)
                    } else {
                        val number = Integer.parseInt(pathArray[i])
                        ChildNumber(number, false)
                    }
                    dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber)
                }
                val keyPair = ECKeyPair.create(dkKey.privKeyBytes)
                val c = Credentials.create(keyPair.privateKey.toString(RADIX))
                val wallet = W3JLWallet(
                    mnemonic = mnemonics,
                    source = Source.MNEMONIC,
                    address = c.address,
                    privateKey = keyPair.privateKey.toString(RADIX),
                    publicKey = keyPair.publicKey.toString(RADIX),
                    jsonSource = "",
                    createAt = Calendar.getInstance().timeInMillis,
                )

                emitter.onSuccess(wallet)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    @Throws(Exception::class)
    override fun createWalletFromMnemonic(
        mnemonics: List<String>,
        password: String?,
    ): Single<W3JLWallet> {
        return createHDWalletFromMnemonic(mnemonics)
            .flatMap { wallet: W3JLWallet ->
                setWalletJsonInfo(wallet, password)
            }
    }

    override fun createWalletFromPrivateKey(
        privateKey: String,
        password: String?,
    ): Single<W3JLWallet> {
        return Single.create { emitter ->
            try {
                if (!WalletUtils.isValidPrivateKey(privateKey)) {
                    emitter.onError(Exception("Invalid private key"))
                    return@create
                }
                val c = Credentials.create(privateKey)
                val wallet = W3JLWallet(
                    source = Source.PRIVATE_KEY,
                    jsonSource = if (password != null) {
                        val tmpWallet = Wallet.create(password, c.ecKeyPair, PARAM_N, PARAM_P)
                        objectMapper.writeValueAsString(tmpWallet)
                    } else {
                        ""
                    },
                    address = c.address,
                    privateKey = privateKey,
                    createAt = Calendar.getInstance().timeInMillis,
                )
                emitter.onSuccess(wallet)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun createWalletFromJsonString(
        jsonString: String,
        password: String,
    ): Single<W3JLWallet> {
        return Single.create { emitter ->
            try {
                val walletFile: WalletFile = objectMapper.readValue(jsonString)
                val c = Credentials.create(Wallet.decrypt(password, walletFile))
                val wallet = W3JLWallet(
                    source = Source.JSON,
                    address = c.address,
                    privateKey = c.ecKeyPair.privateKey.toString(RADIX),
                    jsonSource = jsonString,
                    createAt = Calendar.getInstance().timeInMillis,
                )
                emitter.onSuccess(wallet)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun setWalletJsonInfo(wallet: W3JLWallet, password: String?): Single<W3JLWallet> {
        return Single.create { emitter ->
            if (password == null) {
                // wallet.jsonSource = ""
                emitter.onSuccess(wallet.copy(jsonSource = ""))
            } else {
                val keyPair = Credentials.create(wallet.privateKey).ecKeyPair
                val tmpWallet = Wallet.create(password, keyPair, PARAM_N, PARAM_P)
                //    wallet.jsonSource = objectMapper.writeValueAsString(tmpWallet)
                emitter.onSuccess(wallet.copy(jsonSource = objectMapper.writeValueAsString(tmpWallet)))
            }
        }
    }

    override fun mnemonicsToPrivateKey(mnemonics: String, password: String?): String {
        return mnemonicsToKeyPair(mnemonics, password).privateKey.toString(RADIX)
    }

    override fun mnemonicsToPublicKey(mnemonics: String, password: String): String {
        return mnemonicsToKeyPair(mnemonics, password).publicKey.toString(RADIX)
    }

    override fun mnemonicsToKeyPair(mnemonics: String, password: String?): ECKeyPair {
        val seeds = MnemonicUtils.generateSeed(mnemonics, password)
        return ECKeyPair.create(Hash.sha256(seeds))
    }



    @Throws(IOException::class)
    override fun loadCredential(password: String, file: File): W3JLCredential? {
        val c = WalletUtils.loadCredentials(password, file)
        return W3JLCredential(c.address, c.ecKeyPair.publicKey.toString(RADIX), c.ecKeyPair.privateKey.toString(RADIX))
    }

    override fun loadCredentialFromPrivateKey(privateKey: String): W3JLCredential? {
        return if (!WalletUtils.isValidPrivateKey(privateKey)) {
            null
        } else {
            val c = Credentials.create(privateKey)
            W3JLCredential(c.address, c.ecKeyPair.publicKey.toString(RADIX), c.ecKeyPair.privateKey.toString(RADIX))
        }
    }

    override fun getAddressFromPrivateKey(privateKey: String): String {
        val credentials = loadCredentialFromPrivateKey(privateKey) ?: return ""
        return credentials.address
    }
}
