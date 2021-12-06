package com.rakesh.newsappsample.domain.model.newsfeed

import com.rakesh.newsappsample.data.remote.model.newsfeed.Source
import com.rakesh.newsappsample.domain.misc.Error
import com.rakesh.newsappsample.domain.misc.Result

class NewsArticle private constructor(
    val title: String,
    val urlToImage: String,
    val description: String,
    val author: String,
    val url: String,
    val publishedAt: String,
    val source: NewsSource
) {

    companion object {
        fun create(
            title: String,
            urlToImage: String?,
            description: String?,
            author: String?,
            url: String?,
            publishedAt: String?,
            source: Source?
        ): Result<NewsArticle> {
            return Result.Success(
                NewsArticle(
                    title = if (title.isNotBlank()) title else return Result.Failure(Error.Malformed("News Title is not Valid")),
                    urlToImage = if (!urlToImage.isNullOrEmpty()) urlToImage else return Result.Failure(Error.Malformed("News imageUrl is not Valid")),
                    description = if (!description.isNullOrEmpty()) description else return Result.Failure(Error.Malformed("News description is not Valid")),
                    author = if (!author.isNullOrEmpty()) author else return Result.Failure(Error.Malformed("News author is not Valid")),
                    url = if (!url.isNullOrEmpty()) url else return Result.Failure(Error.Malformed("News url is not Valid")),
                    publishedAt = if (!publishedAt.isNullOrEmpty()) publishedAt else return Result.Failure(Error.Malformed("News publishedAt is not Valid")),
                    source = when (val result = NewsSource.create(source?.id, source?.name)) {
                        is Result.Success -> result.data
                        is Result.Failure -> return Result.Failure(result.error)
                    }
                )
            )
        }
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val tempItem = other as? NewsArticle

        tempItem?.let {
            return (title == tempItem.title) &&
                    (description == tempItem.description) &&
                    (url == tempItem.url) &&
                    (urlToImage == tempItem.urlToImage) &&
                    (author == tempItem.author) &&
                    (publishedAt == tempItem.publishedAt) &&
                    (source.id == tempItem.source.id) && //We can also move source equals logic into its own class
                    (source.name == tempItem.source.name)
        } ?: return false
    }
}