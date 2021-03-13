package com.androiddevs.mvvmnewsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.models.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*

class ArticleAdapter() : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val items: View):RecyclerView.ViewHolder(items)

    private val differCallBack =object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false))
    }
    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currItem = differ.currentList[position]
        holder.items.apply {
            tvTitle.setText(currItem.title)
            Glide.with(holder.items.context).load(currItem.urlToImage).into(ivArticleImage)
            tvPublishedAt.setText(currItem.publishedAt)
            tvDescription.setText(currItem.description)
            tvSource.setText(currItem.source?.name)
            setOnClickListener {
                onItemClickListener?.let { it(currItem) }
            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

}

