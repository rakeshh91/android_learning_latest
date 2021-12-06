package com.rakesh.newsappsample.data.repository.base

import com.rakesh.newsappsample.data.misc.NetworkUtils
import com.rakesh.newsappsample.domain.misc.Error
import com.rakesh.newsappsample.domain.misc.Result
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeoutException

abstract class BaseRemoteRepository(
    private val network: NetworkUtils
) {

    companion object {
        const val NETWORK_ERROR = "NETWORK ERROR"
        const val UNKNOWN_ERROR = "UNKNOWN ERROR"
    }

    suspend fun <W, V> processRequest(
        webRequest: suspend () -> Response<V>,
        mapper: (V) -> Result<W>
    ): Result<W> {
        return processWebRequest(webRequest).map(mapper)
    }

    private suspend fun <T> processWebRequest(
        request: suspend () -> Response<T>
    ): Result<T> =
        try {
            network.isNetworkAvailable().let { networkAvailable ->
                if (!networkAvailable) {
                    Timber.e(NETWORK_ERROR)
                    return Result.Failure(Error.NoConnectivity())
                }

                request.invoke().let {
                    when {
                        it.isSuccessful -> it.body()?.let { data -> Result.Success(data) } ?: Result.Failure(Error.Unknown())
                        else -> {
                            return when (it.code()) {
                                //We can handle 401 and 429 error use cases in a better way using Authenticators
                                in 400..499 -> {
                                    Result.Failure(Error.BadRequest(code = it.code()))
                                }
                                in 500..599 -> {
                                    Result.Failure(Error.SourceFailure(code = it.code()))
                                }
                                else -> Result.Failure(Error.Unknown(code = it.code()))
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is IOException, is TimeoutException ->
                    if (network.isNetworkAvailable()) {
                        Result.Failure(Error.TimeOut(e = e))
                    } else {
                        Result.Failure(Error.NoConnectivity(e))
                    }
                else -> {
                    Timber.d(UNKNOWN_ERROR)
                    Result.Failure(Error.Unknown(e = e))
                }
            }
        }
}