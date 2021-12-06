package com.rakesh.newsappsample.data.remote.mapper.newsfeed

import com.rakesh.newsappsample.data.remote.mapper.base.BaseMapper
import com.rakesh.newsappsample.data.remote.model.newsfeed.NewsListRemoteResponse
import com.rakesh.newsappsample.domain.misc.Error
import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle

class NewsListRemoteResponseMapper(
    private val apiResponse: NewsListRemoteResponse?,
) : BaseMapper<NewsListRemoteResponse, List<NewsArticle>>() {

    override fun map(): Result<List<NewsArticle>> {
        if (apiResponse?.status == ERROR_STATUS) return Result.Failure(Error.Unknown("Status is error"))

        val resultList = mutableListOf<NewsArticle>()

        if (apiResponse?.articles?.isNullOrEmpty() == false) {
            apiResponse.articles.forEach { newsModel ->
                when (val result = NewsArticle.create(
                    newsModel.title,
                    newsModel.urlToImage,
                    newsModel.description,
                    newsModel.author,
                    newsModel.url,
                    newsModel.publishedAt,
                    newsModel.source
                )) {
                    is Result.Success -> resultList.add(result.data)
                    else -> Unit //Ignore invalid news article
                }
            }
        }

        return Result.Success(resultList.toList())
    }

    companion object {
        const val ERROR_STATUS = "error"
    }
}