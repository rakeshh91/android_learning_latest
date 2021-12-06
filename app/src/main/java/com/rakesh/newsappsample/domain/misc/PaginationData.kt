package com.rakesh.newsappsample.domain.misc

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.rakesh.newsappsample.presentation.util.ResponseState

data class PaginationData<T : Any>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val loadFailState: LiveData<ResponseState<Unit>>
)