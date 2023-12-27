package com.example.newsapicase.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.newsapicase.R
import com.example.newsapicase.data.model.Article
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        val url = getDataFromMain()
        openWebView(url)
    }


    private fun getDataFromMain() : String {
        val article = intent.extras?.get("url") as String
        return article
    }

    private fun openWebView(url: String) {
        val myWebView = WebView(this@NewsDetailActivity)
        setContentView(myWebView)
        myWebView.loadUrl(url)
    }
}
