package com.rakesh.newsappsample.data.repository.newsfeed.newspaging3

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rakesh.newsappsample.data.database.AppDatabase
import com.rakesh.newsappsample.data.remote.model.newsfeed.NewsListRemoteModel
import com.rakesh.newsappsample.data.remote.model.newsfeed.Source
import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class NewsRemoteMediator(
    private val appDb: AppDatabase,
    private val newsRemoteRepository: NewsRemoteRepository,
    private val PAGE_SIZE: Int
) : RemoteMediator<Int, NewsListRemoteModel>() {

    private var pageNum: Int = 1

    private val newsDao = appDb.getNewsListDao()

    override suspend fun initialize(): InitializeAction {

        pageNum = 1
        //Refresh the data everytime paging is initialized
        return InitializeAction.LAUNCH_INITIAL_REFRESH

//        val cacheTimeout = 30 * 60 * 1000L
//        return if (System.currentTimeMillis() - userDao.lastUpdated() >= cacheTimeout) {
//            // Cached data is up-to-date, so there is no need to re-fetch from network.
//            InitializeAction.SKIP_INITIAL_REFRESH
//        } else {
//            // Need to refresh cached data from network; returning LAUNCH_INITIAL_REFRESH here
//            // will also block RemoteMediator's APPEND and PREPEND from running until REFRESH
//            // succeeds.
//            InitializeAction.LAUNCH_INITIAL_REFRESH
//        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, NewsListRemoteModel>): MediatorResult {
        return try {
            // The network load method takes an optional [String] parameter. For every page
            // after the first, we pass the [String] token returned from the previous page to
            // let it continue from where it left off. For REFRESH, pass `null` to load the
            // first page.
            when (loadType) {
                LoadType.REFRESH -> {
                    pageNum = 1
                    null
                }
                // In this example, we never need to prepend, since REFRESH will always load the
                // first page in the list. Immediately return, reporting end of pagination.
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                // Query remoteKeyDao for the next RemoteKey.
                LoadType.APPEND -> {
                    pageNum++

                    //If we met the max pageSize then return pagination reached
                    if (pageNum >= 100) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    pageNum
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be wrapped in a
            // withContext(Dispatcher.IO) { ... } block since Retrofit's Coroutine CallAdapter
            // dispatches on a worker thread.

            when (val result = newsRemoteRepository.getNewsList(pageNum, PAGE_SIZE)) {
                is Result.Success -> {
                    // Store loaded data, and next key in transaction, so that they're always consistent
                    val dataObjects = result.data.map { domainModel ->
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

                    appDb.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            newsDao.deleteAll()
                        }
                        // Insert new users into database, which invalidates the current
                        // PagingData, allowing Paging to present the updates in the DB.
                        newsDao.insertAll(dataObjects)
                    }
                    MediatorResult.Success(endOfPaginationReached = pageNum == 100)
                }

                is Result.Failure -> {
                    //Put back the pageNum so that you can request data again from same page
                    pageNum--
                    MediatorResult.Error(Exception(result.error.reason))
                }
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}