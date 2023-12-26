package com.example.newsapicase.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapicase.data.model.Article
import com.example.newsapicase.data.repository.NewsServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainActivityVM @Inject constructor(private val repository: NewsServiceRepository): ViewModel() {

    private var liveData: MutableLiveData<List<Article>> = MutableLiveData()

    fun getLiveDataObserver(): MutableLiveData<List<Article>> {
        return liveData
    }

     suspend fun loadData() {
        repository.getGeneralNews().let {
            it.body().let { news ->
                liveData.postValue(news?.articles)
            }
        }
    }
}