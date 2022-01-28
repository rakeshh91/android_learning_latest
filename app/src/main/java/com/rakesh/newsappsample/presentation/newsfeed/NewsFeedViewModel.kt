package com.rakesh.newsappsample.presentation.newsfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rakesh.newsappsample.domain.misc.PaginationData
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.domain.usecase.newsfeed.GetNewsFeedFlowUseCase
import com.rakesh.newsappsample.domain.usecase.newsfeed.GetNewsFeedUseCase
import com.rakesh.newsappsample.domain.usecase.newsfeed.GetPagedNewsFeedUseCase
import com.rakesh.newsappsample.presentation.base.BaseViewModel
import com.rakesh.newsappsample.presentation.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val getPagedNewsFeedUseCase: GetPagedNewsFeedUseCase,
    private val getNewsFeedUseCase: GetNewsFeedUseCase,
    private val getNewsFeedFlowUseCase: GetNewsFeedFlowUseCase
) : BaseViewModel() {

    /**
     * Getting paginated data from remote + db
     */
    private val newsFeedList2: PaginationData<NewsArticle> by lazy {
        getPagedNewsFeedUseCase.getNewsFeedPaging2(GetPagedNewsFeedUseCase.Params(viewModelScope))
    }

    private val newsFeedList3: Flow<PagingData<NewsArticle>> by lazy {
        getPagedNewsFeedUseCase.getNewsFeedPaging3()
    }

    fun getPaged2NewsFeed(): PaginationData<NewsArticle> {
        return newsFeedList2
    }

    fun getPaged3NewsFeed(): Flow<PagingData<NewsArticle>> {
        return newsFeedList3
    }

    //-----------------------------------------------------------------------------------------------------
    /**
     * Default way of getting data from remote + db
     */

    private val newsfeedResponseState by lazy { MutableLiveData<ResponseState<List<NewsArticle>>>() }

    val newsFeedByPage: LiveData<ResponseState<List<NewsArticle>>> = newsfeedResponseState

    fun fetchNewsFeedByPage(page: Int, pageSize: Int) {
        fetchData(
            getNewsFeedUseCase,
            GetNewsFeedUseCase.Params(page, pageSize),
            newsfeedResponseState
        )
    }

    //-----------------------------------------------------------------------------------------------------
    /**
     * Default way of getting Flow data from remote + db
     */

    private val newsfeedFlowResponseState by lazy { MutableStateFlow<ResponseState<List<NewsArticle>>>(ResponseState.Success(emptyList())) }

    val newsFeedByPageFlow: StateFlow<ResponseState<List<NewsArticle>>> = newsfeedFlowResponseState

    fun fetchNewsFeedByPageFlow(page: Int, pageSize: Int) {
        fetchFlowData(
            getNewsFeedFlowUseCase,
            GetNewsFeedFlowUseCase.Params(page, pageSize),
            newsfeedFlowResponseState
        )
    }
}