package com.example.mnemonicwallet.data.repository

import com.example.mnemonicwallet.data.model.W3JLCredential
import com.example.mnemonicwallet.data.model.W3JLWallet
import io.reactivex.Single
import org.web3j.crypto.ECKeyPair
import java.io.File
import java.io.IOException

interface WalletRepository {
    fun createMnemonics(entropy: ByteArray): String
    fun createMnemonics(): String
    fun mnemonicsToPrivateKey(mnemonics: String, password: String?): String
    fun mnemonicsToPublicKey(mnemonics: String, password: String): String
    fun mnemonicsToKeyPair(mnemonics: String, password: String?): ECKeyPair

    @Throws(IOException::class)
    fun loadCredential(password: String, file: File): W3JLCredential?
    fun loadCredentialFromPrivateKey(privateKey: String): W3JLCredential?
    fun getAddressFromPrivateKey(privateKey: String): String?

    // fun createHDWalletFromMnemonic(mnemonics: String) : Single<W3JLWallet>
    fun createWalletFromMnemonic(mnemonics: List<String>, password: String?): Single<W3JLWallet>
    fun createWalletFromPrivateKey(privateKey: String, password: String?): Single<W3JLWallet>
    fun createWalletFromJsonString(jsonString: String, password: String): Single<W3JLWallet>
}
