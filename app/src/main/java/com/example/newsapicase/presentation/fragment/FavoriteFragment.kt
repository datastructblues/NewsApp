package com.example.newsapicase.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapicase.R
import com.example.newsapicase.presentation.main.MainActivity
import com.example.newsapicase.presentation.main.MainActivityVM
import com.example.newsapicase.presentation.main.NewsAdapter

class FavoriteFragment : Fragment() {

    companion object {
        const val TAG = "FavoriteFragment"
    }

    lateinit var viewModel: MainActivityVM
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val listView = view.findViewById<RecyclerView>(R.id.rvFavorite)
        listView.layoutManager = LinearLayoutManager(requireContext())

        newsAdapter = NewsAdapter(emptyList(), TAG, NewsAdapter.OnClickListener {
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
        }){
            viewModel.deleteArticle(it)
        }
        listView.adapter = newsAdapter

        viewModel.getSavedNews().observe(viewLifecycleOwner) { savedNews ->
            newsAdapter.updateList(savedNews)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }
}