package com.example.mnemonicwallet.ui.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mnemonicwallet.data.model.W3JLWallet
import com.example.mnemonicwallet.data.repository.W3JLFactory
import com.example.mnemonicwallet.utiles.ResultWrapper
import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator
import com.maximeroussy.invitrode.WordGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MnemonicsViewModel @Inject constructor() : ViewModel() {
    private val _wallet: MutableLiveData<ResultWrapper<W3JLWallet>> = MutableLiveData()
    val wallet: LiveData<ResultWrapper<W3JLWallet>> = _wallet

    private val _mnemonic: MutableLiveData<List<String>> = MutableLiveData()
    val mnemonics: LiveData<List<String>> = _mnemonic

    init {
        createMnemonics(12)
    }

    private fun createMnemonics(numbers: Int) {
        val nmes = mutableListOf<String>()
        repeat(numbers) {
            val generator = WordGenerator()
            val myNewWord = generator.newWord(6)
            val mnemonic = RandomWordGenerator.getRandomWord()
            nmes.add(myNewWord)
        }
        _mnemonic.postValue(nmes)
    }

    fun createWallet(context: Context) {
        createMnemonics(12)
        val w3JL = W3JLFactory()
            .withContext(context)
            .buildW3JLWallet()
        CompositeDisposable().add(
            w3JL.createWalletFromMnemonic(_mnemonic.value!!, "niendeptrai")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _wallet.postValue(ResultWrapper.Loading) }
                .doOnSuccess { wallet -> _wallet.postValue(ResultWrapper.Success(wallet)) }
                .doOnError { e ->
                    _wallet.postValue(
                        ResultWrapper.Error(
                            e.localizedMessage ?: "null"
                        )
                    )
                }
                .subscribe()
        )
    }
}
