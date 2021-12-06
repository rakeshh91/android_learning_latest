package com.rakesh.newsappsample.domain.usecase.newsfeed

import com.rakesh.newsappsample.domain.misc.PaginationData
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsPagedRepository
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsPaging3Repository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

//We can migrate from LiveData<PagedList<T> to Flow<PagedList<T> so that we don't need to add any Android dependency into Domain module
class GetPagedNewsFeedUseCase @Inject constructor(
    private val newsPagedRepository: NewsPagedRepository,
    private val newsPaging3Repository: NewsPaging3Repository

) {
    fun getNewsFeedPaging2(param: Params): PaginationData<NewsArticle> = newsPagedRepository.getPagedNewsList(coroutineScope = param.coroutineScope)

    fun getNewsFeedPaging3() = newsPaging3Repository.getPagedNewsList()

    data class Params(val coroutineScope: CoroutineScope)
}