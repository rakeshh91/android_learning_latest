package com.rakesh.newsappsample.domain.repository.newsfeed

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsPaging3Repository {
    fun getPagedNewsList(): Flow<PagingData<NewsArticle>>
}