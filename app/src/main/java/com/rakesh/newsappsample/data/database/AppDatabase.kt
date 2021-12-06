package com.rakesh.newsappsample.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rakesh.newsappsample.data.remote.model.newsfeed.NewsListRemoteModel

@Database(
    entities = [NewsListRemoteModel::class],
    version = 1, exportSchema = false
)
@TypeConverters()
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNewsListDao(): NewsDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context, useInMemory: Boolean): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context, useInMemory).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, useInMemory: Boolean): AppDatabase {
            val databaseBuilder = if (useInMemory) {
                //Use in-memory database in order to erase data once process is killed
                Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            } else {
                Room.databaseBuilder(context, AppDatabase::class.java, "news-db")
            }

            return databaseBuilder
                .build()
        }
    }
}