package com.example.newsapicase.data.repository

import com.example.newsapicase.data.api.NewsService
import javax.inject.Inject

class NewsServiceRepository @Inject constructor(
    private val newsService: NewsService
) {

    suspend fun getGeneralNews() = newsService.getGeneralNews(
        "us",
        "business",
        1
    )
}