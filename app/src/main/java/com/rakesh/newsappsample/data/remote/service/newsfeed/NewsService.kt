package com.rakesh.newsappsample.data.remote.service.newsfeed

import com.rakesh.newsappsample.data.remote.model.newsfeed.NewsListRemoteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("/v2/everything")
    suspend fun getTopNewsList(
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("q") source: String = TOPIC
    ): Response<NewsListRemoteResponse>

    //We can maintain below constants separately based on different build types instead of hardcoding here
    companion object {
        const val ENDPOINT = "https://newsapi.org/"
        const val API_KEY = "ad64a227a4334245855ab5084d03a452"
        const val TOPIC = "Covid"
    }
}