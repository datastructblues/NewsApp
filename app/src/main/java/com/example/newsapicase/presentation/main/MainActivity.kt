package com.example.newsapicase.presentation.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapicase.DEFAULT_CATEGORY
import com.example.newsapicase.DEFAULT_DELAY
import com.example.newsapicase.R
import com.example.newsapicase.data.api.NetworkState
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.data.model.NewsResponse
import com.example.newsapicase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        observeNewsLiveData(viewModel.categoryNews)
        observeNewsLiveData(viewModel.searchNews)
        setSearchViewListener()
        swipeRefresh()
    }

    private fun observeNewsLiveData(newsLiveData: MutableLiveData<NetworkState<NewsResponse>>) {
        newsLiveData.observe(this) { state ->
            when (state) {
                is NetworkState.Loading -> {
                    println("Loading")
                }
                is NetworkState.Success -> {
                    println("başarılı request")
                    state.data?.articles?.let { articles -> showNewsDataInRecyclerView(articles) }
                }
                is NetworkState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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
            println(list.size)

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
        var job: Job? = null
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(DEFAULT_DELAY)
                    query?.let {
                        if (query.isNotEmpty()) {
                            viewModel.searchNews(query)
                        }
                    }
                }
               // viewModel.searchNews(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //optionally you can call searchNews here but for lesser request i prefer the upper one -Umut
                return true
            }
        })
    }
}





