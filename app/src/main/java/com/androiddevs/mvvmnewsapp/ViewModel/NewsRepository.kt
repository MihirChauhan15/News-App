package com.androiddevs.mvvmnewsapp.ViewModel

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import com.androiddevs.mvvmnewsapp.api.RetroFitInstance
import com.androiddevs.mvvmnewsapp.api.RetroFitInstance.Companion.api
import com.androiddevs.mvvmnewsapp.articleDatabase.ArticleDataBase
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import retrofit2.Response
import retrofit2.Retrofit

class NewsRepository(val db:ArticleDataBase){
    suspend fun getAllBreakingNews(countryCode:String,pageNumber:Int):Response<NewsResponse> = RetroFitInstance.api.getAllBreakingNews(countryCode,pageNumber)

    suspend fun getAllSearchNews(searchQuery:String,pageNumber: Int):Response<NewsResponse> =RetroFitInstance.api.getAllSearchNews(searchQuery,pageNumber)

    suspend fun insert(article: Article) = db.getArticleDao.insert(article)

    fun getAllArticles() = db.getArticleDao.getAllArticles()

    suspend fun deleteArticle(article: Article) =db.getArticleDao.delete(article)
}