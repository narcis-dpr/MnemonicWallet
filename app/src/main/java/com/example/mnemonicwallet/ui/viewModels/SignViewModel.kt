package com.example.mnemonicwallet.ui.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mnemonicwallet.data.model.W3JLTokenInfo
import com.example.mnemonicwallet.data.repository.W3JLFactory
import com.example.mnemonicwallet.utiles.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _sign: MutableLiveData<ResultWrapper<String>> = MutableLiveData()
    val sign: LiveData<ResultWrapper<String>> = _sign
    private var privateKey: String = ""
    private var address: String = ""
    init {
        savedStateHandle.get<String>("privateKey")?.let { privKey ->
            privateKey = privKey
        }
        savedStateHandle.get<String>("address")?.let { adrs ->
            address = adrs
        }
    }
    fun signMessage(
        context: Context,
        from: String,
        to: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        nonce: Long,
    ) {
        val w3JL = W3JLFactory()
            .withContext(context)
            .withNetworkProvider("https://eth-mainnet.g.alchemy.com/v2/PZFVExkKL4Bspbxqil3SZzDDbXb7y072")
            .withTokenInfo(W3JLTokenInfo(address = address, name = "tester", symbol = "eth"))
            .buildW3JLToken()
        CompositeDisposable().add(
            w3JL.transferToken(
                from, to, privateKey, amount, gasPrice, gasLimit, nonce)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _sign.postValue(ResultWrapper.Loading) }
                .doOnSuccess { keySign -> _sign.postValue(ResultWrapper.Success(keySign)) }
                .doOnError { e ->
                    _sign.postValue(
                        ResultWrapper.Error(
                            e.localizedMessage ?: "null"
                        )
                    )
                }
                .subscribe()

        )
    }
}
