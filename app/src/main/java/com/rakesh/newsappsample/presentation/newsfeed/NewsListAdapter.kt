package com.rakesh.newsappsample.presentation.newsfeed

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle

class NewsListAdapter(
    private val newsItemClickListener: NewsListItemView.NewsItemClickListener
) : ListAdapter<NewsArticle, NewsItemViewHolder>(DiffCallbackNewsArticle()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        return NewsItemViewHolder(NewsListItemView(parent.context), newsItemClickListener)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem = getItem(position)
        newsItem.let {
            holder.bind(newsItem)
        }
    }
}

private class DiffCallbackNewsArticle : DiffUtil.ItemCallback<NewsArticle>() {

    override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
        return oldItem.title == newItem.title
    }
}

/** Additional methods that can be used when we are using RecyclerView.Adapter instead of ListAdapter */

//    private var items = mutableListOf<NewsArticle>()
//
//    fun getItems(): List<NewsArticle> = items
//
//    /**
//     * This method is used to when data is  set to the adapter for the first time
//     */
//    fun setItems(list: List<NewsArticle>) {
//        items.clear()
//        items.addAll(list)
//    }
//
//    fun updateItemAtPosition(position: Int, updatedItem: NewsArticle) {
//        if (position < getItems().size) {
//            items[position] = updatedItem
//            notifyItemChanged(position)
//        }
//    }
//
//    override fun getItemCount(): Int = getItems().size
//
//    /**
//     * This method is used to add new items to the current adapter data¬
//     */
//    fun appendNewItems(newItems: List<NewsArticle>) {
//        val prevItemCount = itemCount
//        val tempList = mutableListOf<NewsArticle>()
//        for (item in newItems) {
//            if (!getItems().contains(item)) {
//                tempList.add(item)
//            }
//        }
//
//        if (tempList.isNotEmpty()) {
//            items.addAll(tempList)
//            notifyItemRangeInserted(prevItemCount, tempList.size)
//        }
//    }