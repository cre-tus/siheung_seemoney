package com.example.siheung_seemoney.data.model

/**
 * 홈 화면에서 사용하는 재정 요약 데이터 모델.
 */
data class FinanceSummaryResponse(
    val totalBudget: Long,           // 전체 예산 (원 단위)
    val usedBudget: Long,            // 표시할 기준 예산 (타이머 시작점, 원 단위)
    val totalDebt: Long,             // 초기 부채 (원 단위)
    val currentDebt: Long,           // 표시할 기준 부채 (타이머 시작점, 원 단위)
    val annualBudgetDecrease: Long,  // 연간 예산 소진량 (원 단위) -> 초당 감소율 계산용
    val annualDebtIncrease: Long,    // 연간 부채 증가량 (원 단위) -> 초당 증가율 계산용
    val categories: List<BudgetCategory>
)

data class BudgetCategory(
    val name: String,         // 카테고리명 (예: "복지")
    val percentage: Int       // 비율 (예: 40)
)
