package com.example.mnemonicwallet.data.repository

import android.content.Context
import com.example.mnemonicwallet.data.model.W3JLTokenInfo
import io.reactivex.Single
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.utils.Numeric
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

class TokenService(private var context: Context, var web3j: Web3j, var mTokenInfo: W3JLTokenInfo?) : TokenRepository {

    override fun setTokenInfo(tokenInfo: W3JLTokenInfo) {
        this.mTokenInfo = tokenInfo
    }

    override fun getBalance(address: String): Single<BigDecimal> {
        return getBalance(address, mTokenInfo!!)
    }

    override fun getBalance(address: String, tokenInfo: W3JLTokenInfo): Single<BigDecimal> {
        return Single.create { emitter ->
            val function = balanceOf(address)
            val responseValue = callSmartContractFunction(function, tokenInfo.address, address)
            val response = FunctionReturnDecoder.decode(
                responseValue,
                function.outputParameters,
            )
            if (response.size == 1) {
                emitter.onSuccess(BigDecimal((response[0] as Uint256).value))
            } else {
                emitter.onError(Exception("Unknown"))
            }
        }
    }

    override fun transferToken(
        from: String,
        to: String,
        privateKey: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        nonce: Long
    ): Single<String> {
        return Single.create { emitter ->
            try {
                val data = createTokenTransferData(to, amount)
                val signedMessage = BaseConfigService(context)
                    .signTransaction(from, privateKey, mTokenInfo!!.address, BigInteger.valueOf(0), gasPrice, gasLimit, nonce.toLong(), data, 3)
                emitter.onSuccess(
                    Numeric.toHexString(signedMessage)
                )
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun callSmartContractFunction(function: Function, contractAddress: String, address: String): String {
        val encodedFunction = FunctionEncoder.encode(function)
        val response = web3j.ethCall(
            Transaction.createEthCallTransaction(address, contractAddress, encodedFunction),
            DefaultBlockParameterName.LATEST,
        )
            .sendAsync().get()
        return response.value
    }

    private fun balanceOf(owner: String): Function {
        return Function(
            "balanceOf",
            listOf(Address(owner)),
            listOf(object : TypeReference<Uint256>() {
            }),
        )
    }

    private fun createTokenTransferData(to: String, tokenAmount: BigInteger): ByteArray {
        val params = listOf(Address(to), Uint256(tokenAmount))
        val returnTypes = listOf<TypeReference<*>>(object : TypeReference<Bool>() {
        })
        val function = Function("transfer", params, returnTypes)
        val encodedFunction = FunctionEncoder.encode(function)
        return Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(encodedFunction))
    }
}
