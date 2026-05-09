package com.example.siheung_seemoney.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit 클라이언트 싱글톤
 */
object RetrofitClient {

    // TODO: 실제 서버 IP로 변경하세요
    // 로컬 테스트: http://10.0.2.2:8081/api/ (Android 에뮬레이터)
    // 실제 서버: http://YOUR_SERVER_IP:8081/api/
    private const val BASE_URL = "http://10.0.2.2:8081/api/"

    /**
     * HTTP 로깅 인터셉터 (디버그용)
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * OkHttp 클라이언트
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Retrofit 인스턴스
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * API 서비스 인스턴스 (인증 및 활동)
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    /**
     * 재정 관련 API 서비스 인스턴스
     */
    val financeApiService: FinanceApiService by lazy {
        retrofit.create(FinanceApiService::class.java)
    }
}

