package com.rakesh.newsappsample.data.repository.newsfeed

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.rakesh.newsappsample.data.database.NewsDao
import com.rakesh.newsappsample.data.remote.model.newsfeed.NewsListRemoteModel
import com.rakesh.newsappsample.data.remote.model.newsfeed.Source
import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import com.rakesh.newsappsample.presentation.util.ResponseState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsPageDataSource @Inject constructor(
    private val newsRemoteRepository: NewsRemoteRepository,
    private val newsDao: NewsDao,
    private val coroutineScope: CoroutineScope
) : PageKeyedDataSource<Int, NewsArticle>() {

    val loadFailState = MutableLiveData<ResponseState<Unit>>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NewsArticle>
    ) {
        loadFailState.postValue(ResponseState.Loading(true))
        fetchData(page = 1, pageSize = params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NewsArticle>) {
        loadFailState.postValue(ResponseState.Loading(true))
        val page = params.key
        fetchData(page = page, pageSize = params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NewsArticle>) {
        val page = params.key
        fetchData(page, params.requestedLoadSize) {
            callback.onResult(it, page - 1)
        }
    }

    private fun fetchData(page: Int, pageSize: Int, callback: (List<NewsArticle>) -> Unit) {
        coroutineScope.launch {
            when (val response = newsRemoteRepository.getNewsList(page, pageSize)) {
                is Result.Failure -> {
                    loadFailState.postValue(ResponseState.Loading(false))

                    loadFailState.postValue(
                        ResponseState.Failure(response.error)
                    )
                }
                is Result.Success -> {
                    loadFailState.postValue(ResponseState.Loading(false))

                    val results = response.data
                    loadFailState.postValue(ResponseState.Success(Unit))

                    //This implementation is debatable..
                    //We can either put entire response to DB or we can only put the successfully mapped data into DB
                    val dataObjects = results.map { domainModel ->
                        NewsListRemoteModel(
                            domainModel.title,
                            domainModel.urlToImage,
                            domainModel.description,
                            domainModel.author,
                            domainModel.url,
                            domainModel.publishedAt,
                            Source(domainModel.source.id, domainModel.source.name)
                        )
                    }

                    newsDao.insertAll(dataObjects)

                    callback(results)

                }
            }
        }
    }
}
