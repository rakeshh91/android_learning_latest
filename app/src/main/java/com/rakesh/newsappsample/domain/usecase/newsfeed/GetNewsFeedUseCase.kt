package com.rakesh.newsappsample.domain.usecase.newsfeed

import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import com.rakesh.newsappsample.domain.usecase.UseCase
import javax.inject.Inject

class GetNewsFeedUseCase @Inject constructor(
    private val newsRemoteRepository: NewsRemoteRepository
) : UseCase<GetNewsFeedUseCase.Params, List<NewsArticle>> {

    override suspend fun execute(param: Params): Result<List<NewsArticle>> =
        newsRemoteRepository.getNewsList(param.page, param.pageSize)

    data class Params(val page: Int, val pageSize: Int)
}