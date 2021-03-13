package com.androiddevs.mvvmnewsapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repository: NewsRepository):ViewModel(){

    var breakingNews: MutableLiveData<Resource<NewsResponse> > = MutableLiveData()
    var breakingPageNumber:Int = 1
    var searchNews: MutableLiveData<Resource<NewsResponse> > =MutableLiveData()
    var searchPageNumber:Int = 1
    var breakingNewsResponse:NewsResponse? = null
    var searchNewsResponse:NewsResponse? = null
    init{
        getAllBreakingNews("in")
    }
    fun getAllBreakingNews(countryCode:String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = repository.getAllBreakingNews(countryCode,breakingPageNumber)
        breakingNews.postValue(handleBreakingNews(response))
    }

    private fun handleBreakingNews(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                breakingPageNumber++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }
                else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticles?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())

    }
    fun getAllSearchNews(searchQuery:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.getAllSearchNews(searchQuery,searchPageNumber)
        searchNews.postValue(handleSearchNews(response))
    }
    private fun handleSearchNews(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                searchPageNumber++
                if(searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                }
                else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticles?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())

    }
    fun insert(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }
    fun getAllSavedArticle() =repository.getAllArticles()

    fun deleteArticle(article: Article) =viewModelScope.launch {
        repository.deleteArticle(article)
    }
}