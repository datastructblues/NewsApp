package com.example.newsapicase.presentation.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import com.example.newsapicase.DEFAULT_DELAY
import com.example.newsapicase.R
import com.example.newsapicase.databinding.ActivityMainBinding
import com.example.newsapicase.onNavigationButtonClicked
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
        setToolbar()
        startNewsFragment()
        setSearchViewListener()
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

    private fun startNewsFragment() {
        onNavigationButtonClicked(binding.bottomNavigation.menu.findItem(R.id.news))
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

    private fun setToolbar() {
        val toolbar = findViewById<View>(R.id.my_toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        toolbar.hideOverflowMenu()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}





