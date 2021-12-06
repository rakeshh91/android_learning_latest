package com.rakesh.newsappsample.presentation.newsfeed

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle

//PagedListAdapter
class NewsPagedListAdapter(
    private val newsItemClickListener: NewsListItemView.NewsItemClickListener
) : PagedListAdapter<NewsArticle, NewsItemViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem = getItem(position)
        newsItem?.let {
            holder.bind(newsItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        return NewsItemViewHolder(NewsListItemView(parent.context), newsItemClickListener)
    }
}

private class DiffCallback : DiffUtil.ItemCallback<NewsArticle>() {

    override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
        return oldItem.title == newItem.title
    }
}