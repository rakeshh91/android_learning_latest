package com.rakesh.newsappsample.presentation.newsfeed

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.rakesh.newsappsample.databinding.ItemNewsListBinding
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle
import com.rakesh.newsappsample.presentation.util.ImageHelper

class NewsListItemView(context: Context) : ConstraintLayout(context) {

    private lateinit var binding: ItemNewsListBinding

    init {
        inflateCustomLayout()
    }

    fun inflateCustomLayout() {
        binding = ItemNewsListBinding.inflate(LayoutInflater.from(context), this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).also {
            it.topMargin = 20
        }
    }

    fun setClickListener(newsItemClickListener: NewsItemClickListener) {
        binding.cardViewNews.setOnClickListener {
            newsItemClickListener.onNewsItemClicked(binding.cardViewNews.tag.toString())
        }
    }

    fun updateDataOnView(article: NewsArticle) {
        binding.textViewNewsDescription.text = article.description
        binding.textViewNewsTitle.text = article.title
        binding.cardViewNews.tag = article.title
        ImageHelper.loadImage(binding.imageViewNews, article.urlToImage)
    }

    interface NewsItemClickListener {
        fun onNewsItemClicked(newsId: String)
    }
}