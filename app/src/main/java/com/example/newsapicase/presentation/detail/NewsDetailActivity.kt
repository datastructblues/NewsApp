package com.example.newsapicase.presentation.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.example.newsapicase.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        val url = getDataFromMain()
        setToolbar()
        openWebView(url)
    }

    private fun getDataFromMain(): String {
        val article = intent.extras?.getString("url") ?: ""
        return article
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebView(url: String) {
        val myWebView = findViewById<WebView>(R.id.webview)
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl(url)
    }

    private fun setToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
