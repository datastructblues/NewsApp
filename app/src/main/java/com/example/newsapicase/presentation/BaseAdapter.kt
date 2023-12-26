package com.example.newsapicase.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapicase.R
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.databinding.NewsItemBinding
import com.example.newsapicase.loadImage

class BaseAdapter(
    private val newsList: List<Article>,
    private val onClickListener: OnClickListener
) : ListAdapter<Article, BaseAdapter.ViewHolder>(DiffCallback()) {

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
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (photoList: Article) -> Unit) {
        fun onClick(article: Article) = clickListener(article)
    }
}
