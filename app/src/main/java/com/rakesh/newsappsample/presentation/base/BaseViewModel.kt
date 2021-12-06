package com.rakesh.newsappsample.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.usecase.UseCase
import com.rakesh.newsappsample.presentation.util.ResponseState
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    //open fun <T : Any> processData(data: T): T = data

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
}