package com.example.mnemonicwallet.utiles

import java.lang.Exception

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: String) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}

val <T> ResultWrapper<T>.data: T?
    get() {
        return (this as? ResultWrapper.Success)?.data
    }
