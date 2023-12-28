package com.example.newsapicase.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.example.newsapicase.R
import com.example.newsapicase.data.model.Article

class DetailFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init MainActivityVM
        //viewModel = (activity as MainActivity).viewModel

        //get data from bundle
        val article = arguments?.getSerializable("article") as Article
        setViews(article)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    private fun setViews(article: Article) {
        //set views
        view?.findViewById<AppCompatImageView>(R.id.imageView)?.let {
            Glide.with(this).load(article.urlToImage).into(it)
        }
        view?.findViewById<AppCompatTextView>(R.id.titleTextView)?.text = article.title
        view?.findViewById<AppCompatTextView>(R.id.descriptionTextView)?.text = article.description
        view?.findViewById<AppCompatTextView>(R.id.authorTextView)?.text = article.author
        view?.findViewById<AppCompatTextView>(R.id.publishedAtTextView)?.text = article.publishedAt
        view?.findViewById<AppCompatTextView>(R.id.contentTextView)?.text = article.content

    }
}