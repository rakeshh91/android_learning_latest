package com.rakesh.newsappsample.data.repository.newsfeed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.rakesh.newsappsample.data.database.NewsDao
import com.rakesh.newsappsample.data.misc.NetworkUtils
import com.rakesh.newsappsample.domain.misc.PaginationData
import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsPagedRepository
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import com.rakesh.newsappsample.presentation.util.ResponseState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsPagedRepositoryImpl @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val remoteRepositoryImpl: NewsRemoteRepository,
    private val newsDao: NewsDao
) : NewsPagedRepository {

    override fun getPagedNewsList(coroutineScope: CoroutineScope): PaginationData<NewsArticle> {
        return if (networkUtils.isNetworkAvailable()) {
            //Remote data
            getPagedNewsFromRemote(coroutineScope)
        } else {
            //Local DB Data
            getPagedNewsFromLocalDatabase()
        }
    }

    private fun getPagedNewsFromRemote(ioCoroutineScope: CoroutineScope): PaginationData<NewsArticle> {
        val dataSourceFactory = NewsPageDataSourceFactory(remoteRepositoryImpl, newsDao, ioCoroutineScope)
        val loadFailState = Transformations.switchMap(dataSourceFactory.liveData) {
            it.loadFailState
        }
        return PaginationData(LivePagedListBuilder(dataSourceFactory, NewsPageDataSourceFactory.pagedListConfig()).build(), loadFailState)
    }

    private fun getPagedNewsFromLocalDatabase(): PaginationData<NewsArticle> {
        val dataSourceFactory = newsDao.getPagedNews().mapByPage { newsRemoteModelList ->
            //Map responses stored in DB to Domain Model objects that UI is expecting
            val mutableList = mutableListOf<NewsArticle>()
            newsRemoteModelList.forEach {
                val res = NewsArticle.create(
                    it.title,
                    it.urlToImage,
                    it.description,
                    it.author,
                    it.url,
                    it.publishedAt,
                    it.source
                )
                if (res is Result.Success) {
                    mutableList.add(res.data)
                }
            }
            mutableList
        }

        val loadFailState = MutableLiveData<ResponseState<Unit>>()
        loadFailState.postValue(ResponseState.Loading(false))
        loadFailState.postValue(ResponseState.Success(Unit))

        return PaginationData(
            LivePagedListBuilder(
                dataSourceFactory,
                NewsPageDataSourceFactory.pagedListConfig()
            ).build(), loadFailState
        )

    }
}

