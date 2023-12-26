package com.example.newsapicase.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapicase.R
import com.example.newsapicase.data.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

     val viewModel: MainActivityVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.getLiveDataObserver().observe(this) {
            if (it != null) {
                showDataInRecyclerView(it)
            }
        }
        GlobalScope.launch {
            viewModel.loadData()
        }


    }


    private fun showDataInRecyclerView(list: List<Article>) {
        //iterate list
        for (article in list) {
            println(article.urlToImage)
        }

        //bazı url'lerin linki yok o zaman patlıyor.

        val recyclerView: RecyclerView = findViewById(R.id.listView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BaseAdapter(list,BaseAdapter.OnClickListener{
            println(it.title)
        })
        }
}





