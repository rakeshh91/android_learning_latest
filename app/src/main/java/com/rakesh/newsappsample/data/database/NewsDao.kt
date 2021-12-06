package com.rakesh.newsappsample.data.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*
import com.rakesh.newsappsample.data.remote.model.newsfeed.NewsListRemoteModel
import com.rakesh.newsappsample.domain.model.newsfeed.NewsArticle

/**
 * NewsListRemoteModel is the data model we get from backend and we store into database directly
 *
 * Whenever we are passing the info to UI, we convert it to Domain object NewsArticle
 */
@Dao
interface NewsDao {
    @Query("Select * from NewsListRemoteModel")
    fun getNews(): LiveData<List<NewsListRemoteModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newsList: List<NewsListRemoteModel>)

    @Query("DELETE FROM NewsListRemoteModel")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: NewsListRemoteModel)

    @Query("SELECT * FROM NewsListRemoteModel")
    fun getPagedNews(): DataSource.Factory<Int, NewsListRemoteModel>

    @Query("SELECT * FROM NewsListRemoteModel")
    fun getPaged3News(): PagingSource<Int, NewsListRemoteModel>
}