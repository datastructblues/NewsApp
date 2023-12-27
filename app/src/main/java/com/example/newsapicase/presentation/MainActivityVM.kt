package com.example.newsapicase.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.data.repository.NewsServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

     suspend fun loadData(
        category: String
     ) {
        repository.getGeneralNews(category).let {
            it.body().let { news ->
                liveData.postValue(news?.articles)
            }
        }
    }
}