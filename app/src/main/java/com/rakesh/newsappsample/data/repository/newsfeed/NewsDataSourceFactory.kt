package com.rakesh.newsappsample.data.repository.newsfeed

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.rakesh.newsappsample.data.database.NewsDao
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsPageDataSourceFactory @Inject constructor(
    private val dataSource: NewsRemoteRepository,
    private val dao: NewsDao,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, NewsArticle>() {

    val liveData = MutableLiveData<NewsPageDataSource>()

    override fun create(): DataSource<Int, NewsArticle> {
        val source = NewsPageDataSource(dataSource, dao, scope)
        liveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 20
        fun pagedListConfig() = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }
}