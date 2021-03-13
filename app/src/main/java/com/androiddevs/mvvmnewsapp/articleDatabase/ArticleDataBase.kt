package com.androiddevs.mvvmnewsapp.articleDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.models.Article
import java.util.concurrent.locks.Lock

@Database(entities = [Article::class], version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDataBase : RoomDatabase() {

    abstract val getArticleDao:ArticleDao

    companion object {

        @Volatile
        private var INSTANCE: ArticleDataBase? = null
        private val Lock = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(Lock) {
            INSTANCE ?: createDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                ArticleDataBase::class.java,
                "article_db.db").fallbackToDestructiveMigration().build()
    }



}