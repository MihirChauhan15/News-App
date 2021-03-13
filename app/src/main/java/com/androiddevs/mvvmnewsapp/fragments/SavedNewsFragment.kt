package com.androiddevs.mvvmnewsapp.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.ArticleAdapter
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ViewModel.NewsViewModel
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
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

            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle)
        }

        val itemCallbackHelper =object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsadapter.differ.currentList[position]
                viewModel.deleteArticle( article)

                Snackbar.make(view, "Article deleted successfully ", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.insert(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemCallbackHelper).apply {
            attachToRecyclerView(rvSavedNews)
        }
        viewModel.getAllSavedArticle().observe(viewLifecycleOwner, Observer {
            newsadapter.differ.submitList(it)
        })

    }
    private fun setUpRecyclerView(){
        newsadapter = ArticleAdapter()
        rvSavedNews.apply {
            adapter =newsadapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}