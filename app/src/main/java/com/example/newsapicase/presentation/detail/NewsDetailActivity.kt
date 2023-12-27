package com.example.newsapicase.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapicase.R
import com.example.newsapicase.data.model.Article
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        getDataFromMain()
    }


    private fun getDataFromMain() {
        val intent = intent
        val serializableExtra = intent.getSerializableExtra("news") as Article?

        if (serializableExtra != null) {
            for (i in 0..10) {
                println(serializableExtra.title)
            }
        } else {
            // Handle the case where "news" extra is not an instance of Article or is null
            // For example, you can show an error message or finish the activity.
            finish()
        }
    }
}
