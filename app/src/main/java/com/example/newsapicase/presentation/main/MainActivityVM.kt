package com.example.newsapicase.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapicase.DEFAULT_CATEGORY
import com.example.newsapicase.data.api.NetworkState
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.data.model.NewsResponse
import com.example.newsapicase.data.repository.NewsServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainActivityVM @Inject constructor(private val repository: NewsServiceRepository): ViewModel() {

    val categoryNews: MutableLiveData<NetworkState<NewsResponse>> = MutableLiveData()
    var categoryNewsPage = 1

    val searchNews: MutableLiveData<NetworkState<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        loadNews(DEFAULT_CATEGORY)
    }


    fun loadNews(category: String) = viewModelScope.launch {
        categoryNews.postValue(NetworkState.Loading())
        repository.getGeneralNews(category).let {
            categoryNews.postValue(handleCategoryNews(it))
        }
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(NetworkState.Loading())
        val response = repository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleCategoryNews(response: Response<NewsResponse>) : NetworkState<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return NetworkState.Success(resultResponse)
            }
        }
        return NetworkState.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : NetworkState<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return NetworkState.Success(resultResponse)
            }
        }
        return NetworkState.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews() = repository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }
}