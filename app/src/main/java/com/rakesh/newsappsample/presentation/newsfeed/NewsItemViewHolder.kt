package com.rakesh.newsappsample.presentation.newsfeed

import androidx.recyclerview.widget.RecyclerView
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle

class NewsItemViewHolder(
    private val newsItemView: NewsListItemView,
    private val newsItemClickListener: NewsListItemView.NewsItemClickListener
) : RecyclerView.ViewHolder(newsItemView) {

    init {
        newsItemView.apply {
            setClickListener(newsItemClickListener)
        }
    }

    fun bind(item: NewsArticle) {
        newsItemView.updateDataOnView(item)
    }
}