package com.example.newsapicase.data.repository

import com.example.newsapicase.data.api.NewsService
import javax.inject.Inject

class NewsServiceRepository @Inject constructor(
    private val newsService: NewsService
) {

    suspend fun getGeneralNews(category: String) = newsService.getGeneralNews(
        "us",
        category,
        1
    )

    suspend fun searchNews(searchQuery: String, pageNum: Int) = newsService.searchNews(
        searchQuery,
        pageNum)
}