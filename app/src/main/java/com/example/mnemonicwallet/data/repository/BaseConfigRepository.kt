package com.example.mnemonicwallet.data.repository

import org.ethereum.geth.Account
import java.math.BigInteger

interface BaseConfigRepository {

    fun createAccount(password: String): Account
    fun importKeystore(store: String, password: String, newPassword: String): Account
    fun importPrivateKey(privateKey: String, newPassword: String): Account
    fun signTransaction(
        signer: String,
        signerPrivateKey: String,
        toAddress: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        nonce: Long,
        data: ByteArray,
        chainId: Long,
    ): ByteArray

    fun hasAccount(address: String): Boolean
    fun fetchAccounts(): Array<String?>
}
