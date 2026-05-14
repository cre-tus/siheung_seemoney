package com.example.siheung_seemoney.data

import retrofit2.http.GET

/**
 * 뉴스 API 서비스 인터페이스
 */
interface NewsApiService {
    @GET("v1/news")
    suspend fun getNews(): List<News>
}
