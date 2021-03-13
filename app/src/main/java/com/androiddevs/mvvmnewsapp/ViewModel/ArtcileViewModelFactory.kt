package com.androiddevs.mvvmnewsapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ArtcileViewModelFactory(val newsRepository: NewsRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}