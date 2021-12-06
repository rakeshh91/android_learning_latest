package com.rakesh.newsappsample.di.modules

import com.rakesh.newsappsample.data.database.AppDatabase
import com.rakesh.newsappsample.data.database.NewsDao
import com.rakesh.newsappsample.data.misc.NetworkUtils
import com.rakesh.newsappsample.data.remote.service.newsfeed.NewsService
import com.rakesh.newsappsample.data.repository.newsfeed.NewsPagedRepositoryImpl
import com.rakesh.newsappsample.data.repository.newsfeed.NewsRemoteRepositoryImpl
import com.rakesh.newsappsample.data.repository.newsfeed.newspaging3.NewsPaging3RepositoryImpl
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsPagedRepository
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsPaging3Repository
import com.rakesh.newsappsample.domain.repository.newsfeed.NewsRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
object NewsModule {

    @Singleton
    @Provides
    fun provideNewsService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): NewsService {
        return Retrofit.Builder()
            .baseUrl(NewsService.ENDPOINT)
            .addConverterFactory(converterFactory)
            .client(okhttpClient)
            .build()
            .create(NewsService::class.java)
    }

    @Singleton
    @Provides
    fun provideNewsRemoteRepository(newsService: NewsService, networkUtils: NetworkUtils): NewsRemoteRepository =
        NewsRemoteRepositoryImpl(networkUtils, newsService)

    @Singleton
    @Provides
    fun provideNewsPagedRepository(networkUtils: NetworkUtils, newsRemoteRepository: NewsRemoteRepository, newsDao: NewsDao): NewsPagedRepository =
        NewsPagedRepositoryImpl(networkUtils, newsRemoteRepository, newsDao)

    @Singleton
    @Provides
    fun provideNewsSetDao(db: AppDatabase) = db.getNewsListDao()

    @Singleton
    @Provides
    fun provideNewsPaging3RepositoryImpl(appDb: AppDatabase, newsRemoteRepository: NewsRemoteRepository): NewsPaging3Repository =
        NewsPaging3RepositoryImpl(appDb, newsRemoteRepository)
}