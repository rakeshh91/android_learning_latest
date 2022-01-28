package com.rakesh.newsappsample.domain.usecase

import com.rakesh.newsappsample.domain.misc.Result
import kotlinx.coroutines.flow.Flow

//this is not used in this project but still keeping here for reference.
interface BaseUseCase<in Param, out Data>

interface UseCase<in Param, out Data> : BaseUseCase<Param, Data> {

    suspend fun execute(param: Param): Result<Data>
}

interface FlowUseCase<in Param, out Data> : BaseUseCase<Param, Data> {

    suspend fun execute(param: Param): Flow<Result<Data>>
}