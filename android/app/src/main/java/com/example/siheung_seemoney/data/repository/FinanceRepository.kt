package com.example.siheung_seemoney.data.repository

import com.example.siheung_seemoney.data.model.BudgetCategory
import com.example.siheung_seemoney.data.model.CategoryBudget
import com.example.siheung_seemoney.data.model.FinanceSummaryResponse
import com.example.siheung_seemoney.R

/**
 * 홈 화면 재정 데이터의 출처를 관리하는 Repository.
 * 지금은 MOCK 데이터를 반환하고, API 연동 시 이 파일만 수정하면 됨.
 */
class FinanceRepository {

    companion object {
        // ================================================================
        // MOCK DATA - 백엔드 API 연동 전 임시 가짜 데이터
        // API 연동 완료 후 이 companion object 블록 전체 삭제할 것
        // ================================================================
        val MOCK = FinanceSummaryResponse(
            totalBudget          = 1_600_000L,
            usedBudget           = 1_580_000L,
            totalDebt            = 120_000L,
            currentDebt          = 125_000L,
            annualBudgetDecrease = 1_600_000L,  // 연간 전체 예산이 소진됨 (실제값)
            annualDebtIncrease   = 50_000L,     // 연간 부채 5만억원 증가 추정 (실제값)
            categories           = listOf(
                BudgetCategory("복지", 40),
                BudgetCategory("교통", 25),
                BudgetCategory("환경", 15),
                BudgetCategory("기타", 20)
            )
        )
        // ================================================================
        // MOCK DATA 끝
        // ================================================================
    }

    fun getFinanceSummary(): FinanceSummaryResponse {
        return MOCK

        // API 연동 시: 위 줄 삭제하고 아래 주석 해제
        // return apiService.getFinanceSummary()
    }

    /**
     * 분석 화면에 표시할 분야별 예산 데이터를 반환합니다.
     * 현재는 UI 구성을 위한 가짜 데이터(Mock Data)를 반환하고 있으며, 
     * 차후 API가 완성되면 apiService.getCategoryBudgets() 등으로 변경할 예정입니다.
     */
    fun getCategoryBudgets(): List<CategoryBudget> {
        return listOf(
            CategoryBudget("복지", 2093649200000L, 40, 5.2, R.drawable.bg_progress_blue),
            CategoryBudget("교통", 1308530750000L, 25, 8.1, R.drawable.bg_progress_green),
            CategoryBudget("환경", 785118450000L, 15, -2.3, R.drawable.bg_progress_orange),
            CategoryBudget("기타", 1046824600000L, 20, 1.5, R.drawable.bg_progress_purple)
        )
    }
}
