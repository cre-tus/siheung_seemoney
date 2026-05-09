package com.example.siheung_seemoney.data.model

data class BudgetSummaryResponse(
    val year: Int,
    val totalAmount: Long,
    val categories: List<BudgetCategorySummaryDto>
)

data class BudgetCategorySummaryDto(
    val categoryId: Long,
    val categoryName: String,
    val amount: Long,
    val ratio: Double,
    val changeRate: Double,
    val trend: String
)
