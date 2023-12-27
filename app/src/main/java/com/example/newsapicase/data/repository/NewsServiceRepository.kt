package com.example.newsapicase.data.repository

import com.example.newsapicase.data.api.NewsService
import com.example.newsapicase.data.db.ArticleDAO
import com.example.newsapicase.data.db.ArticleDatabase
import com.example.newsapicase.data.model.Article
import javax.inject.Inject

class NewsServiceRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao : ArticleDAO
) {

    suspend fun getGeneralNews(category: String) = newsService.getGeneralNews(
        "us",
        category,
        1
    )

    suspend fun searchNews(searchQuery: String, pageNum: Int) = newsService.searchNews(
        "us",
        searchQuery,
        pageNum
    )

    suspend fun upsert(article: Article) = articleDao.upsert(article)

    fun getSavedNews() = articleDao.getAllArticles()

    suspend fun deleteArticle(article: Article) = articleDao.deleteArticles(article)
}