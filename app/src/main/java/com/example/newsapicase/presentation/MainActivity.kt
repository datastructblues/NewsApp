package com.example.newsapicase.presentation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapicase.DEFAULT_CATEGORY
import com.example.newsapicase.R
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityVM by viewModels()
    private lateinit var newsAdapter: BaseAdapter
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCategoriesButtonsAndNews()
        viewModel.getLiveDataObserver().observe(this) {
            if (it != null) {
                showNewsDataInRecyclerView(it)
            }
        }
        loadNews(DEFAULT_CATEGORY)
        swipeRefresh()
    }

    private fun initCategoriesButtonsAndNews() {
        val categories = resources.getStringArray(R.array.categories)
        val rv: RecyclerView = findViewById(R.id.category_list)
        val adapter = CategoryAdapter(this, categories) { category ->
            println("Category clicked: $category")
            loadNews(category)
        }
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val recyclerView: RecyclerView = findViewById(R.id.listView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = BaseAdapter(emptyList(), BaseAdapter.OnClickListener {
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

    @SuppressLint("NotifyDataSetChanged")
    private fun swipeRefresh() {
        binding.refresher.setOnRefreshListener {
            loadNews(DEFAULT_CATEGORY)
            binding.refresher.isRefreshing = false
        }
    }

    private fun loadNews(category: String) {
        viewModel.loadData(category)
    }
}





