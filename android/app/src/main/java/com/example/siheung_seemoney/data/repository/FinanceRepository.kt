package com.example.siheung_seemoney.data.repository

import com.example.siheung_seemoney.data.model.BudgetLiveResponse
import com.example.siheung_seemoney.data.model.BudgetSummaryResponse
import com.example.siheung_seemoney.data.model.FinanceSummaryResponse
import com.example.siheung_seemoney.data.model.CategoryBudget
import com.example.siheung_seemoney.data.network.RetrofitClient
import com.example.siheung_seemoney.R
import kotlin.collections.emptyList

/**
 * 홈 화면 재정 데이터의 출처를 관리하는 Repository.
 * 지금은 MOCK 데이터를 반환하고, API 연동 시 이 파일만 수정하면 됨.
 */
class FinanceRepository {

    companion object {
        // 부채(Debt) 관련 임시 가짜 데이터 (API 미구현 상태)
        const val MOCK_TOTAL_DEBT = 120_000L
        const val MOCK_CURRENT_DEBT = 125_000L
        const val MOCK_ANNUAL_DEBT_INCREASE = 50_000L
    }

    suspend fun getFinanceSummary(): FinanceSummaryResponse {
        return try {
            // 라이브 예산은 최신 연도(2026) 기준으로 가져옴
            val apiData = RetrofitClient.financeApiService.getLiveBudget(2026)
            
            // 초당 변화량을 연간 변화량으로 역산 (원 단위 -> 백만원 단위 변환 포함)
            val amountPerYear = (apiData.amountPerSecond * 365.0 * 24 * 3600).toLong() / 1_000_000

            FinanceSummaryResponse(
                totalBudget = apiData.totalAmount / 1_000_000,
                usedBudget = apiData.remainingAmount / 1_000_000, // 홈 화면 타이머: 남은 예산부터 감소하도록 설정
                totalDebt = MOCK_TOTAL_DEBT,
                currentDebt = MOCK_CURRENT_DEBT,
                annualBudgetDecrease = amountPerYear,
                annualDebtIncrease = MOCK_ANNUAL_DEBT_INCREASE,
                categories = emptyList()
            )
        } catch (e: Exception) {
            // 통신 실패 시 빈 데이터 반환 (임시 예외 처리)
            e.printStackTrace()
            FinanceSummaryResponse(
                totalBudget = 0L,
                usedBudget = 0L,
                totalDebt = MOCK_TOTAL_DEBT,
                currentDebt = MOCK_CURRENT_DEBT,
                annualBudgetDecrease = 0L,
                annualDebtIncrease = MOCK_ANNUAL_DEBT_INCREASE,
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
                    budget = dto.amount, // 분석 화면 리스트는 '원' 단위 텍스트 사용
                    percentage = dto.ratio.toInt(), // 백엔드에서 이미 0~100 사이 값으로 반환됨
                    changeRate = dto.changeRate,
                    colorResId = colorResIds[index % colorResIds.size]
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 에러 시 빈 리스트 반환하여 시각적으로 데이터가 비었음을 표시
            emptyList()
        }
    }
}
