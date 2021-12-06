package com.rakesh.newsappsample.domain.repository.newsfeed

import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle

//This domain level interface is not required if we are using Paginated data since pagination repository has its own interface
interface NewsRemoteRepository {
    suspend fun getNewsList(page: Int, pageSize: Int): Result<List<NewsArticle>>
}