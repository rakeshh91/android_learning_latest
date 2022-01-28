package com.rakesh.newsappsample.domain.usecase.newsfeed

import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import com.rakesh.newsappsample.domain.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsFeedFlowUseCase @Inject constructor(
    private val newsRemoteRepository: NewsRemoteRepository
) : FlowUseCase<GetNewsFeedFlowUseCase.Params, List<NewsArticle>> {

    override suspend fun execute(param: Params): Flow<Result<List<NewsArticle>>> =
        newsRemoteRepository.getNewsListFlow(param.page, param.pageSize)

    data class Params(val page: Int, val pageSize: Int)
}