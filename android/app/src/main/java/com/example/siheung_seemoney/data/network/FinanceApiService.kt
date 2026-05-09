package com.example.siheung_seemoney.data.network

import com.example.siheung_seemoney.data.model.BudgetLiveResponse
import com.example.siheung_seemoney.data.model.BudgetSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FinanceApiService {
    
    // 예산 요약 (라이브 타이머용)
    @GET("budgets/live")
    suspend fun getLiveBudget(@Query("year") year: Int): BudgetLiveResponse

    // 분야별 카테고리 예산 요약
    @GET("budgets/summary")
    suspend fun getBudgetSummary(@Query("year") year: Int): BudgetSummaryResponse
}
