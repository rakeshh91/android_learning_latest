package com.rakesh.newsappsample.domain.misc

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Failure<out T>(val error: Error) : Result<T>()

    fun <U> map(parse: (T) -> Result<U>): Result<U> = when (this) {
        is Success<T> -> parse(this.data)
        is Failure<T> -> Failure(this.error)
    }
}

sealed class Error(val reason: String, val e: Exception?, val code: Int? = null) {

    class NoConnectivity(e: Exception? = null) : Error("No internet connection", e)
    class Malformed(reason: String, e: Exception? = null) : Error(reason, e)
    class TimeOut(e: Exception? = null) : Error("Api timeout", e)
    class BadRequest(e: Exception? = null, code: Int? = null) : Error("Bad Request", e, code)
    class SourceFailure(e: Exception? = null, code: Int? = null) : Error("Source Failure", e)
    class Unknown(reason: String = "", e: Exception? = null, code: Int? = null) : Error(reason, e, code)

}