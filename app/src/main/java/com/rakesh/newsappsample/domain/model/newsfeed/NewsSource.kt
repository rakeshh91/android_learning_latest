package com.rakesh.newsappsample.domain.model.newsfeed

import com.rakesh.newsappsample.domain.misc.Error
import com.rakesh.newsappsample.domain.misc.Result

class NewsSource private constructor(
    val id: String,
    val name: String
) {
    companion object {
        fun create(
            id: String?,
            name: String?
        ): Result<NewsSource> {
            return Result.Success(
                NewsSource(
                    id = if (!id.isNullOrEmpty()) id else "",
                    name = if (!name.isNullOrEmpty()) name else return Result.Failure(Error.Malformed("Source name is not Valid")),
                )
            )
        }
    }
}