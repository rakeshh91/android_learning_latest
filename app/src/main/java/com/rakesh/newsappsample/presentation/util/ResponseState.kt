package com.rakesh.newsappsample.presentation.util

import com.rakesh.newsappsample.domain.misc.Error

sealed class ResponseState<out R> {
    data class Loading(val loading: Boolean, val message: String? = null) : ResponseState<Nothing>()
    data class Success<T>(val data: T) : ResponseState<T>()
    data class Failure(val error : Error) : ResponseState<Nothing>()
}