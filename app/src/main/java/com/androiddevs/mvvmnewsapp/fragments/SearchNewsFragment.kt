package com.androiddevs.mvvmnewsapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.ArticleAdapter
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ViewModel.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_breaking_news.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.util.Constants
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
class SearchNewsFragment: Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsadapter: ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        setUpRecyclerView()
        var job: Job? =null
        button.setOnClickListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                etSearch.text?.let {
                    if(etSearch.text.toString().isNotEmpty()){
                        viewModel.getAllSearchNews(etSearch.text.toString())
                    }
                }
            }
        }




        newsadapter.setOnItemClickListener {
            val bundle=Bundle().apply{
                putSerializable("article",it)
            }

            findNavController().navigate(R.id.action_searchNews_to_articleFragment,bundle)
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    paginationProgressBar.visibility =View.INVISIBLE
                    response.data?.let{
                        newsadapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults/ Constants.QUERY_PAGE_SIZE +2
                        isAtLastPage = totalPages == viewModel.searchPageNumber
                        if(isAtLastPage){
                            rvSearchNews.setPadding(0,0,0,0)
                          //  Toast.makeText(context,"End of the Page",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Resource.Error->{
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading =false
                    response.message?.let {
                        Log.e("Error:","$it")
                    }
                }
                is Resource.Loading->{
                    paginationProgressBar.visibility = View.VISIBLE
                    isLoading =true
                }
            }

        })


    }

    var isAtLastPage:Boolean = false
    var isLoading:Boolean =false
    var isScrolling=false
    val OnScrollListener =object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling =true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val visibleItemCount =layoutManager.childCount
            val firstVisibleItemPosition =layoutManager.findFirstVisibleItemPosition()

            val isNotLoadingAndNotLastPage = !isLoading && !isAtLastPage
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isAtLastItem = firstVisibleItemPosition+visibleItemCount>=totalItemCount
            val isTotalMoreThanVisible =totalItemCount>= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isNotLoadingAndNotLastPage && isScrolling
            if(shouldPaginate){
                viewModel.getAllSearchNews(etSearch.text.toString())
                isScrolling =false
            }

        }
    }
    private fun setUpRecyclerView(){
        newsadapter = ArticleAdapter()
        rvSearchNews.apply {
            adapter =newsadapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.OnScrollListener)
        }
    }

}