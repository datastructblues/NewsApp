package com.example.newsapicase.data.api

import com.example.newsapicase.API_KEY
import com.example.newsapicase.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    //get general
    @GET("v2/top-headlines")
    suspend fun getGeneralNews(
        @Query("country") country: String = "us", //uygulamanin diline gore otomatik filtrele
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int = 100,
        @Query("page") pageNum: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun searchNews(
        @Query("country") country: String = "us",
        @Query("q") searchQuery: String,
        @Query("page") pageNum: Int =1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

}