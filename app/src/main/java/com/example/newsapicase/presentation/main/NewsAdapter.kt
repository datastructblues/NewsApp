package com.example.newsapicase.presentation.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapicase.R
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.databinding.NewsItemBinding
import com.example.newsapicase.loadImage

class NewsAdapter(
    private var newsList: List<Article>,
    private val TAG: String,
    private val onClickListener: OnClickListener,
    private val onFavoriteClickListener: OnFavoriteClickListener
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(newsList[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = NewsItemBinding.bind(itemView)
        fun bind(article: Article) {
            article.urlToImage.let { binding.imageView.loadImage(it) }
            article.title.let{ binding.titleTextView.text = it }
            article.source?.name.let { binding.siteAddressTextView.text = it }
            article.publishedAt.let { binding.date.text = it }
            binding.favoriteButton.setOnClickListener {
                onFavoriteClickListener.onFavoriteClick(article)
            }
            if (TAG == "FavoriteFragment") {
                binding.favoriteButton.setImageResource(R.drawable.ic_delete)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Article>) {
        //  sharingList = emptyList()
        newsList = newList
        notifyDataSetChanged()
    }
/*
    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
 */
    class OnClickListener(val clickListener: (newsList: Article) -> Unit) {
        fun onClick(article: Article) = clickListener(article)
    }
    fun interface OnFavoriteClickListener {
        fun onFavoriteClick(article: Article)
    }
}
