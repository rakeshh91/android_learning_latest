package com.rakesh.newsappsample.data.repository.newsfeed

import com.rakesh.newsappsample.data.misc.NetworkUtils
import com.rakesh.newsappsample.data.remote.mapper.newsfeed.NewsListRemoteResponseMapper
import com.rakesh.newsappsample.data.remote.service.newsfeed.NewsService
import com.rakesh.newsappsample.data.repository.base.BaseRemoteRepository
import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRemoteRepositoryImpl @Inject constructor(
    network: NetworkUtils,
    private val newsService: NewsService
) : BaseRemoteRepository(network), NewsRemoteRepository {

    override suspend fun getNewsList(page: Int, pageSize: Int): Result<List<NewsArticle>> {
        return processRequest(
            { newsService.getTopNewsList(page = page, pageSize = pageSize) },
            { NewsListRemoteResponseMapper(it).map() }
        )
    }

    override suspend fun getNewsListFlow(page: Int, pageSize: Int): Flow<Result<List<NewsArticle>>> {
        return flow {
            emit(
                processRequest(
                    { newsService.getTopNewsList(page = page, pageSize = pageSize) },
                    { NewsListRemoteResponseMapper(it).map() }
                )
            )
        }
    }
}