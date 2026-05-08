package com.example.siheung_seemoney.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 실제 서버 도메인 주소로 변경 필요. (에뮬레이터 로컬 테스트용 10.0.2.2 등)
    private const val BASE_URL = "http://10.0.2.2:8081"

    val instance: FinanceApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(FinanceApiService::class.java)
    }
}
