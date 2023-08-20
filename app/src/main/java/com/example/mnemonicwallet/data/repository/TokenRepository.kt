package com.example.mnemonicwallet.data.repository

import com.example.mnemonicwallet.data.model.W3JLTokenInfo
import io.reactivex.Single
import java.math.BigDecimal
import java.math.BigInteger

interface TokenRepository {

    fun setTokenInfo(tokenInfo: W3JLTokenInfo)
    fun getBalance(address: String): Single<BigDecimal>
    fun getBalance(address: String, tokenInfo: W3JLTokenInfo): Single<BigDecimal>
    fun transferToken(
        from: String,
        to: String,
        privateKey: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        nonce: Long,
    ): Single<String>
}
