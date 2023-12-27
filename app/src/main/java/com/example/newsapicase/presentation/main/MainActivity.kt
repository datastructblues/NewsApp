package com.example.newsapicase.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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
import com.example.newsapicase.onNavigationButtonClicked
import com.example.newsapicase.presentation.detail.NewsDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainActivityVM by viewModels()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // initCategoriesButtonsAndNews()
     //   observeNewsLiveData(viewModel.categoryNews)
     //   observeNewsLiveData(viewModel.searchNews)
        setSearchViewListener()
       // swipeRefresh()
        onNavigationClicks()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun onNavigationClicks() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            onNavigationButtonClicked(menuItem)
        }
    }

}





