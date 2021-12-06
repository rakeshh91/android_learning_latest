package com.rakesh.newsappsample.presentation.newsfeed.paging3ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.presentation.newsfeed.NewsItemViewHolder
import com.rakesh.newsappsample.presentation.newsfeed.NewsListItemView

class NewsPaging3Adapter(
    private val newsItemClickListener: NewsListItemView.NewsItemClickListener
) : PagingDataAdapter<NewsArticle, NewsItemViewHolder>(diffCallBack) {

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem = getItem(position)
        newsItem?.let {
            holder.bind(newsItem)
        }
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            //We can implement updating single item using payload difference here in future
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        return NewsItemViewHolder(NewsListItemView(parent.context), newsItemClickListener)
    }

    companion object {

        val diffCallBack = object : DiffUtil.ItemCallback<NewsArticle>() {

            override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }
}