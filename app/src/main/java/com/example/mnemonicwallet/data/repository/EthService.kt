package com.example.mnemonicwallet.data.repository

import com.example.mnemonicwallet.utiles.BalanceUtil
import io.reactivex.Single
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.utils.Numeric
import java.io.File
import java.math.BigInteger

class EthService(private var web3j: Web3j) : EthRepository {

    // default gasPrice
    private val GAS_PRICE = BigInteger.valueOf(20_000_000_000L)

    // default gasLimit
    private val GAS_LIMIT = BigInteger.valueOf(4300000)

    override fun getAccountBalance(address: String): Single<BigInteger> {
        return Single.create { emitter ->
            try {
                if (WalletUtils.isValidAddress(address)) {
                    emitter.onError(Exception("Invalid Address"))
                    return@create
                } else {
                    val ethGetBalance: EthGetBalance = web3j
                        .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get()
                    emitter.onSuccess(ethGetBalance.balance)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun getAccountBalance(address: String, number: Int): Single<String> {
        return Single.create { emitter ->
            try {
                if (WalletUtils.isValidAddress(address)) {
                    emitter.onError(Exception("Invalid Address"))
                    return@create
                } else {
                    val ethGetBalance: EthGetBalance = web3j
                        .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get()
                    emitter.onSuccess(BalanceUtil.weiToEth(ethGetBalance.balance, number))
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun getAccountTransactionCount(address: String): Single<BigInteger> {
        return Single.create { emitter ->
            try {
                if (WalletUtils.isValidAddress(address)) {
                    emitter.onError(Exception("Invalid Address"))
                } else {
                    val ethGetTransactionCount: EthGetTransactionCount = web3j
                        .ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get()
                    emitter.onSuccess(ethGetTransactionCount.transactionCount)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun transfer(privateKey: String, to: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger): Single<String> {
        return Single.create { emitter ->
            try {
                val credentials = Credentials.create(privateKey)
                if (credentials == null) {
                    emitter.onError(Exception("Invalid credential!"))
                    return@create
                }
                val from = credentials.address
                val ethGetTransactionCount = web3j.ethGetTransactionCount(
                    from,
                    DefaultBlockParameterName.LATEST,
                ).sendAsync().get()

                val nonce = ethGetTransactionCount.transactionCount
                println(nonce)

                val rawTransaction = RawTransaction.createEtherTransaction(
                    nonce,
                    gasPrice,
                    gasLimit,
                    to,
                    amount,
                )
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)

                val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get()
                emitter.onSuccess(ethSendTransaction.transactionHash)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun transfer(password: String, walletFile: File, to: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger): Single<String> {
        return Single.create { emitter ->
            try {
                val credentials = WalletUtils.loadCredentials(password, walletFile)
                if (credentials == null) {
                    emitter.onError(Exception("Invalid credential!"))
                    return@create
                }
                val from = credentials.address
                val ethGetTransactionCount = web3j.ethGetTransactionCount(
                    from,
                    DefaultBlockParameterName.LATEST,
                ).sendAsync().get()

                val nonce = ethGetTransactionCount.transactionCount
                println(nonce)

                val rawTransaction = RawTransaction.createEtherTransaction(
                    nonce,
                    gasPrice,
                    gasLimit,
                    to,
                    amount,
                )
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)

                val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get()
                emitter.onSuccess(ethSendTransaction.transactionHash)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun transfer(password: String, walletFile: File, to: String, amount: BigInteger): Single<String> {
        return transfer(password, walletFile, to, amount, GAS_PRICE, GAS_LIMIT)
    }

    override fun transfer(privateKey: String, to: String, amount: BigInteger): Single<String> {
        return transfer(privateKey, to, amount, GAS_PRICE, GAS_LIMIT)
    }
}
