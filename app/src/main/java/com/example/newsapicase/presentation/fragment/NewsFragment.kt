package com.example.newsapicase.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapicase.DEFAULT_CATEGORY
import com.example.newsapicase.R
import com.example.newsapicase.data.api.NetworkState
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.data.model.NewsResponse
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
            listView.smoothScrollToPosition(0)
        }
        categoriesView.adapter = adapter
        categoriesView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        listView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(emptyList(), TAG, NewsAdapter.OnClickListener {
            println(it.title)
            /*
            if you want to use webview.
            val intent = Intent(requireContext(), NewsDetailActivity::class.java)
            intent.putExtra("url", it.url)
            startActivity(intent)
             */
            val bundle = Bundle()
            bundle.putSerializable("article", it) // veriyi ekleyin
            val detailFragment = DetailFragment()
            detailFragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.center, detailFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }) {
            viewModel.saveArticle(it)
            Toast.makeText(requireContext(), "Favorilere eklendi", Toast.LENGTH_SHORT).show()
        }
        listView.adapter = newsAdapter
        listView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun showNewsDataInRecyclerView(list: List<Article>) {
        println(list.size)
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