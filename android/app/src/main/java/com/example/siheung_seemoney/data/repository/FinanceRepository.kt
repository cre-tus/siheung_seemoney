package com.example.siheung_seemoney.data.repository

import com.example.siheung_seemoney.data.model.BudgetCategory
import com.example.siheung_seemoney.data.model.FinanceSummaryResponse

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
}
