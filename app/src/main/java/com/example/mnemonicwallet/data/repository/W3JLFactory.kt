package com.example.mnemonicwallet.data.repository

import android.content.Context
import com.example.mnemonicwallet.data.model.W3JLTokenInfo
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.io.File

class W3JLFactory {

    private var context: Context? = null
    private var keyStoreFilePath: String = "keystore"
    private var networkProvider: String = ""
    private var keyStoreFile: File? = null
    private var tokenInfo: W3JLTokenInfo? = null

    fun withContext(context: Context): W3JLFactory {
        this.context = context
        keyStoreFile = File(context.applicationInfo.dataDir, keyStoreFilePath)
        return this
    }


    fun withNetworkProvider(networkProvider: String): W3JLFactory {
        this.networkProvider = networkProvider
        return this
    }

    fun withTokenInfo(tokenInfo: W3JLTokenInfo): W3JLFactory {
        this.tokenInfo = tokenInfo
        return this
    }

    fun buildW3JLWallet(): WalletRepository {
        check(keyStoreFile != null) { "Please init context | keyStoreFile first" }
        return WalletService()
    }

    fun buildW3JLEth(): EthRepository {
        val web3j = Web3jFactory.build(if (networkProvider.isEmpty()) HttpService() else HttpService(networkProvider))
        return EthService(web3j)
    }

    fun buildW3JLToken(): TokenService {
        check(context != null) { "Please init context first, using withContext(context: Context)" }
        check(tokenInfo != null) { "Please init TokenInfo first, using withTokenInfo(tokenInfo: W3JLTokenInfo)" }
        val web3j = Web3jFactory.build(if (networkProvider.isEmpty()) HttpService() else HttpService(networkProvider))
        return TokenService(context!!, web3j, tokenInfo)
    }
}
