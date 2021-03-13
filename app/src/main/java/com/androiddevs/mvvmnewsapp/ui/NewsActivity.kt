package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ViewModel.ArtcileViewModelFactory
import com.androiddevs.mvvmnewsapp.ViewModel.NewsRepository
import com.androiddevs.mvvmnewsapp.ViewModel.NewsViewModel
import com.androiddevs.mvvmnewsapp.articleDatabase.ArticleDataBase
import com.androiddevs.mvvmnewsapp.models.Article
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val repository = NewsRepository(ArticleDataBase(this))
        val viewModelProviderFactory = ArtcileViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(myNavHostFragment.findNavController())


    }

}