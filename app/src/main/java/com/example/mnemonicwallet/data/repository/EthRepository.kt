package com.example.mnemonicwallet.data.repository

import io.reactivex.Single
import org.web3j.crypto.CipherException
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.util.concurrent.ExecutionException

interface EthRepository {

    fun getAccountBalance(address: String): Single<BigInteger>

    fun getAccountBalance(address: String, number: Int): Single<String>

    fun getAccountTransactionCount(address: String): Single<BigInteger>

    @Throws(InterruptedException::class, ExecutionException::class, IOException::class, CipherException::class)
    fun transfer(password: String, walletFile: File, to: String, amount: BigInteger): Single<String>

    @Throws(InterruptedException::class, ExecutionException::class, IOException::class, CipherException::class)
    fun transfer(password: String, walletFile: File, to: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger): Single<String>

    @Throws(InterruptedException::class, ExecutionException::class)
    fun transfer(privateKey: String, to: String, amount: BigInteger): Single<String>

    @Throws(InterruptedException::class, ExecutionException::class)
    fun transfer(privateKey: String, to: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger): Single<String>
}
