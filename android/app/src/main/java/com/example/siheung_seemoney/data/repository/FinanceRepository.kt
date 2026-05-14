package com.example.siheung_seemoney.data.repository

import com.example.siheung_seemoney.data.News
import com.example.siheung_seemoney.data.model.BudgetLiveResponse
import com.example.siheung_seemoney.data.model.BudgetSummaryResponse
import com.example.siheung_seemoney.data.model.FinanceSummaryResponse
import com.example.siheung_seemoney.data.model.CategoryBudget
import com.example.siheung_seemoney.data.network.RetrofitClient
import com.example.siheung_seemoney.R
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.collections.emptyList

/**
 * 홈 화면 재정 데이터의 출처를 관리하는 Repository.
 * 예산: GET /api/budgets/live?year=2026
 * 부채: GET /api/debts/realtime/2026
 */
class FinanceRepository {

    suspend fun getFinanceSummary(): FinanceSummaryResponse {
        return try {
            coroutineScope {
                // 예산과 부채 API를 병렬로 호출하여 속도 최적화
                val budgetDeferred = async { RetrofitClient.financeApiService.getLiveBudget(2026) }
                val debtDeferred = async { RetrofitClient.financeApiService.getRealtimeDebt2026() }

                val budgetData = budgetDeferred.await()
                val debtData = debtDeferred.await()

                // 초당 변화량을 연간 변화량으로 역산 (원 단위 유지)
                val annualBudgetDecrease = (budgetData.amountPerSecond * 365.0 * 24 * 3600).toLong()

                // 부채 연간 감소량 = (시작값 - 종료값)
                val annualDebtDecrease = debtData.startDebt - debtData.endDebt

                FinanceSummaryResponse(
                    totalBudget = budgetData.totalAmount,
                    usedBudget = budgetData.remainingAmount,
                    totalDebt = debtData.startDebt,
                    currentDebt = debtData.currentDebt,
                    annualBudgetDecrease = annualBudgetDecrease,
                    annualDebtIncrease = annualDebtDecrease,
                    categories = emptyList()
                )
            }
        } catch (e: Exception) {
            // API 실패 시 타이머가 멈추지 않도록 기본값 반환 (로그만 출력)
            e.printStackTrace()
            FinanceSummaryResponse(
                totalBudget = 0L,
                usedBudget = 0L,
                totalDebt = 0L,
                currentDebt = 0L,
                annualBudgetDecrease = 0L,
                annualDebtIncrease = 0L,
                categories = emptyList()
            )
        }
    }

    /**
     * 분석 화면에 표시할 분야별 예산 데이터를 반환합니다.
     */
    suspend fun getCategoryBudgets(year: Int): List<CategoryBudget> {
        return try {
            val apiData = RetrofitClient.financeApiService.getBudgetSummary(year)
            val colorResIds = listOf(
                R.drawable.bg_progress_blue,
                R.drawable.bg_progress_green,
                R.drawable.bg_progress_orange,
                R.drawable.bg_progress_purple
            )

            apiData.categories.mapIndexed { index, dto ->
                CategoryBudget(
                    categoryName = dto.categoryName,
                    budget = dto.amount,
                    percentage = dto.ratio.toInt(),
                    changeRate = dto.changeRate,
                    colorResId = colorResIds[index % colorResIds.size]
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * 실시간 재정 뉴스를 가져옵니다.
     */
    suspend fun getNews(): List<News> {
        return try {
            RetrofitClient.newsApiService.getNews()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
