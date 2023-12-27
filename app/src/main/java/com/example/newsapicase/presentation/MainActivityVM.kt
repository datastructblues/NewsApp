package com.example.newsapicase.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.data.repository.NewsServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityVM @Inject constructor(private val repository: NewsServiceRepository): ViewModel() {

    var liveData: MutableLiveData<List<Article>>

    init {
        liveData = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<List<Article>> {
        return liveData
    }

    fun loadData(category: String) = viewModelScope.launch {
        repository.getGeneralNews(category).let {
            it.body().let { news ->
                liveData.postValue(news?.articles)
            }
        }
    }
}