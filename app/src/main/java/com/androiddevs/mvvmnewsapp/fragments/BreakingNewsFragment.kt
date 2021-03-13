package com.androiddevs.mvvmnewsapp.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.Gravity.apply
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.ArticleAdapter
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ViewModel.NewsViewModel
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment:Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsadapter: ArticleAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        setUpRecyclerView()
        newsadapter.setOnItemClickListener {
            val bundle=Bundle().apply{
                putSerializable("article",it)
            }

            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
        }
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    paginationProgressBar.visibility =View.INVISIBLE
                    response.data?.let{
                        newsadapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults/ QUERY_PAGE_SIZE +2
                        isAtLastPage = totalPages == viewModel.breakingPageNumber
                        if(isAtLastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                           // Toast.makeText(context,"End of the Page", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                is Resource.Error->{
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading=false
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
    val OnScrollListener =object :RecyclerView.OnScrollListener(){
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
            val isTotalMoreThanVisible =totalItemCount>=QUERY_PAGE_SIZE

            val shouldPaginate = isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isNotLoadingAndNotLastPage && isScrolling
            if(shouldPaginate){
                viewModel.getAllBreakingNews("in")
                isScrolling =false
            }

        }
    }
    private fun setUpRecyclerView(){
        newsadapter = ArticleAdapter()
        rvBreakingNews.apply {
            adapter =newsadapter
            layoutManager =LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.OnScrollListener)
        }
    }
}