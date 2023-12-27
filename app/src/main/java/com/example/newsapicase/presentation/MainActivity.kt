package com.example.newsapicase.presentation

import android.annotation.SuppressLint
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
    private lateinit var newsAdapter: BaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCategoriesButtonsAndNews()
        viewModel.getLiveDataObserver().observe(this) {
            if (it != null) {
                showNewsDataInRecyclerView(it)
            }
        }
        GlobalScope.launch {
            viewModel.loadData("general")
        }

    }

    private fun initCategoriesButtonsAndNews() {
        val categories = resources.getStringArray(R.array.categories)
        val rv: RecyclerView = findViewById(R.id.category_list)
        val adapter = CategoryAdapter(this, categories) { category ->
            println("Category clicked: $category")
            GlobalScope.launch {
                viewModel.loadData(category)
            }
        }
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val recyclerView: RecyclerView = findViewById(R.id.listView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = BaseAdapter(emptyList(),BaseAdapter.OnClickListener {
            println(it.title)
        })
        recyclerView.adapter = newsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }


    private fun showNewsDataInRecyclerView(list: List<Article>) {
        //iterate list
        for (article in list) {
            println(list.size)
        }
        //bazı url'lerin linki yok o zaman patlıyor.
        newsAdapter.updateList(list)
    }

}





