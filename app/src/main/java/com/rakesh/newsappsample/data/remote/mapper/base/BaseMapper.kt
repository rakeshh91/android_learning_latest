package com.rakesh.newsappsample.data.remote.mapper.base

import com.rakesh.newsappsample.domain.misc.Result

abstract class BaseMapper<T, R> {

    abstract fun map(): Result<R>
}