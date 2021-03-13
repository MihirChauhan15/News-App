package com.androiddevs.mvvmnewsapp.articleDatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.mvvmnewsapp.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article):Long

    @Query("SELECT *FROM ARTICLES")
    fun getAllArticles():LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)
}