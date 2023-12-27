package com.example.newsapicase.presentation.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapicase.DEFAULT_CATEGORY
import com.example.newsapicase.R
import com.example.newsapicase.data.api.NetworkState
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityVM by viewModels()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCategoriesButtonsAndNews()
        viewModel.categoryNews.observe(this) {
            when (it) {
                is NetworkState.Loading -> {
                    println("Loading")
                }
                is NetworkState.Success -> {
                    it.data?.articles?.let { articles -> showNewsDataInRecyclerView(articles) }
                }
                is NetworkState.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.searchNews.observe(this) {
            when (it) {
                is NetworkState.Loading -> {
                    println("Loading")
                }
                is NetworkState.Success -> {
                    it.data?.articles?.let { articles -> showNewsDataInRecyclerView(articles) }
                }
                is NetworkState.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        setSearchViewListener()
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
        newsAdapter = NewsAdapter(emptyList(), NewsAdapter.OnClickListener {
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
        viewModel.loadNews(category)
    }

    private fun setSearchViewListener() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchNews(newText.toString())
                return true
            }
        })
    }
}





