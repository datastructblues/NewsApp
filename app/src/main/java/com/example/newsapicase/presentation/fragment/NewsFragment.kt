package com.example.newsapicase.presentation.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapicase.DEFAULT_CATEGORY
import com.example.newsapicase.R
import com.example.newsapicase.data.api.NetworkState
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.data.model.NewsResponse
import com.example.newsapicase.presentation.detail.NewsDetailActivity
import com.example.newsapicase.presentation.main.CategoryAdapter
import com.example.newsapicase.presentation.main.MainActivity
import com.example.newsapicase.presentation.main.MainActivityVM
import com.example.newsapicase.presentation.main.NewsAdapter

class NewsFragment : Fragment() {


    companion object {
        const val TAG = "NewsFragment"
    }
    lateinit var viewModel: MainActivityVM
    private lateinit var newsAdapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init MainActivityVM
        viewModel = (activity as MainActivity).viewModel
        val listView = view.findViewById<RecyclerView>(R.id.listView)
        val categories = view.findViewById<RecyclerView>(R.id.category_list)
        initCategoriesButtonsAndNews(listView,categories)
        swipeRefresh()
        observeNewsLiveData(viewModel.categoryNews)
        observeNewsLiveData(viewModel.searchNews)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    private fun initCategoriesButtonsAndNews(listView:RecyclerView, categoriesView:RecyclerView) {
        val categories = resources.getStringArray(R.array.categories)
        val adapter = CategoryAdapter(requireContext(), categories) { category ->
            println("Category clicked: $category")
            viewModel.loadNews(category)
        }
        categoriesView.adapter = adapter
        categoriesView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        listView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(emptyList(), TAG, NewsAdapter.OnClickListener {
            println(it.title)
            //viewModel.saveArticle(it)
            val intent = Intent(requireContext(), NewsDetailActivity::class.java)
            intent.putExtra("url", it.url)
            startActivity(intent)

        }) {
            viewModel.saveArticle(it)
            println(it.url)
            println(it.urlToImage)
            println(it.title)
            println(it.id)
            println(it.author)
            println(it.content)
            println(it.description)
            println(it.publishedAt)
            println(it.source)
            Toast.makeText(requireContext(), "Favorilere eklendi", Toast.LENGTH_SHORT).show()
        }
        listView.adapter = newsAdapter
        listView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun showNewsDataInRecyclerView(list: List<Article>) {
        //iterate list
        println(list.size)
        //bazı url'lerin linki yok o zaman patlıyor.
        newsAdapter.updateList(list)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun swipeRefresh() {
        val refresher = view?.findViewById<SwipeRefreshLayout>(R.id.refresher)
        refresher?.setOnRefreshListener {
            viewModel.loadNews(DEFAULT_CATEGORY)
            refresher.isRefreshing = false
        }
    }

    private fun observeNewsLiveData(newsLiveData: MutableLiveData<NetworkState<NewsResponse>>) {
        newsLiveData.observe(requireActivity()) { state ->
            when (state) {
                is NetworkState.Loading -> {
                    println("Loading")
                }
                is NetworkState.Success -> {
                    println("başarılı request")
                    state.data?.articles?.let { articles -> showNewsDataInRecyclerView(articles) }
                }
                is NetworkState.Error -> {
                  println("Error")
                }
            }
        }
    }

}