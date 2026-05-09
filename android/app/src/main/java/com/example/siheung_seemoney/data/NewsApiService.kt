package com.example.siheung_seemoney.data

import retrofit2.Call
import retrofit2.http.GET

/**
 * 뉴스 API 서비스 인터페이스
 */
interface NewsApiService {
    @GET("/api/v1/news")
    fun getNews(): Call<List<News>>
}
