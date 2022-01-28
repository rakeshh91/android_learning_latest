package com.rakesh.newsappsample.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rakesh.newsappsample.domain.misc.Error
import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.usecase.FlowUseCase
import com.rakesh.newsappsample.domain.usecase.UseCase
import com.rakesh.newsappsample.presentation.util.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    //open fun <T : Any> processData(data: T): T = data

    //Uses Livedata
    fun <Param, T : Any> fetchData(
        useCase: UseCase<Param, T>,
        input: Param,
        responseState: MutableLiveData<ResponseState<T>>
    ) {
        responseState.value = ResponseState.Loading(true)
        viewModelScope.launch {
            useCase.execute(input).also {
                when (it) {
                    is Result.Success -> {
                        responseState.postValue(ResponseState.Loading(false))
                        responseState.postValue(ResponseState.Success(it.data))
                        //responseState.postValue(ResponseState.Success(processData(it.data)))
                    }
                    is Result.Failure -> {
                        //We can capture common timeout, unauthorized,network errors in a separate livedata/flow and differentiate them from regular API failures
                        responseState.postValue(ResponseState.Loading(false))
                        responseState.postValue(ResponseState.Failure(it.error))
                    }
                }
            }
        }
    }


    //Uses Flow
    fun <Param, T : Any> fetchFlowData(
        useCase: FlowUseCase<Param, T>,
        input: Param,
        responseState: MutableStateFlow<ResponseState<T>>
    ) {
        viewModelScope.launch {

            useCase.execute(input)
                .onStart {
                    responseState.value = ResponseState.Loading(true)
                }
                .catch { exception ->
                    responseState.value = ResponseState.Loading(false)
                    responseState.value = ResponseState.Failure(Error.Unknown(exception.message ?: "Unknown"))
                }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            responseState.value = ResponseState.Loading(false)
                            responseState.value = ResponseState.Success(it.data)
                            //responseState.postValue(ResponseState.Success(processData(it.data)))
                        }
                        is Result.Failure -> {
                            //We can capture common timeout, unauthorized,network errors in a separate livedata/flow and differentiate them from regular API failures
                            responseState.value = ResponseState.Loading(false)
                            responseState.value = ResponseState.Failure(it.error)
                        }
                    }
                }
        }
    }
}