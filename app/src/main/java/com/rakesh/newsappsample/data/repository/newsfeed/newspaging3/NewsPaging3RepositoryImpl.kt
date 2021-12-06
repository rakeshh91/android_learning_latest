package com.rakesh.newsappsample.data.repository.newsfeed.newspaging3

import androidx.paging.*
import com.rakesh.newsappsample.data.database.AppDatabase
import com.rakesh.newsappsample.domain.misc.Result
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsPaging3Repository
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsPaging3RepositoryImpl @Inject constructor(
    private val appDb: AppDatabase,
    private val newsRemoteRepository: NewsRemoteRepository
) : NewsPaging3Repository {

    companion object {
        private const val PAGE_SIZE = 10
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedNewsList(): Flow<PagingData<NewsArticle>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true, initialLoadSize = PAGE_SIZE),
            remoteMediator = NewsRemoteMediator(appDb, newsRemoteRepository, PAGE_SIZE)
        ) {
            appDb.getNewsListDao().getPaged3News()
        }.flow.map { pagingData ->
            pagingData.map { remoteModel ->
                (NewsArticle.create(
                    remoteModel.title,
                    remoteModel.urlToImage,
                    remoteModel.description,
                    remoteModel.author,
                    remoteModel.url,
                    remoteModel.publishedAt,
                    remoteModel.source
                ) as Result.Success).data
            }
        }
    }
}