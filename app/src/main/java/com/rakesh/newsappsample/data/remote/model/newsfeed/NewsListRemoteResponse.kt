package com.rakesh.newsappsample.data.remote.model.newsfeed

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsListRemoteResponse(
    @Expose
    @SerializedName("source")
    val source: String?,

    @Expose
    @SerializedName("status")
    val status: String?,

    @Expose
    @SerializedName("articles")
    val articles: List<NewsListRemoteModel>?
) : Serializable

@Entity
data class NewsListRemoteModel(

    //Primary key should always be a non-null value when using with room. So, better assign default value if you are expecting a null value from backend
    @PrimaryKey
    @Expose
    @SerializedName("title")
    val title: String = "",

    @Expose
    @SerializedName("urlToImage")
    var urlToImage: String?,

    @Expose
    @SerializedName("description")
    var description: String?,

    @Expose
    @SerializedName("author")
    var author: String?,

    @Expose
    @SerializedName("url")
    var url: String?,

    @Expose
    @SerializedName("publishedAt")
    var publishedAt: String?,

    @Embedded @SerializedName("source")
    val source: Source?
)

data class Source(
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?
)
