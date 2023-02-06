package com.example.newsapp.api

import com.example.newsapp.MainActivity.Companion.API_KEY
import com.example.newsapp.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("/v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "in",
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}