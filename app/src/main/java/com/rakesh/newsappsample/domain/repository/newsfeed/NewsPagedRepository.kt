package com.rakesh.newsappsample.domain.repository.newsfeed

import com.rakesh.newsappsample.domain.misc.PaginationData
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import kotlinx.coroutines.CoroutineScope

interface NewsPagedRepository {

    fun getPagedNewsList(coroutineScope: CoroutineScope): PaginationData<NewsArticle>
}