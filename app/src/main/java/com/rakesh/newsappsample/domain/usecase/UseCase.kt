package com.rakesh.newsappsample.domain.usecase

import com.rakesh.newsappsample.domain.misc.Result

//this is not used in this project but still keeping here for reference.
interface UseCase<in Param, out Data> {

    suspend fun execute(param: Param): Result<Data>
}