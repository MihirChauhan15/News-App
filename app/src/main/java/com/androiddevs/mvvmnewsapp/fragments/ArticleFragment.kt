package com.androiddevs.mvvmnewsapp.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ViewModel.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*


class ArticleFragment: Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args:ArticleFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        val article =args.article
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        fab.setOnClickListener{
            viewModel.insert(article)
            Snackbar.make(it, "Article saved successfully", Snackbar.LENGTH_LONG).show()
        }


    }


}