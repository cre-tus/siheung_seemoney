package com.example.siheung_seemoney.data.model

data class BudgetLiveResponse(
    val year: Int,
    val totalAmount: Long,
    val remainingAmount: Long,
    val usedAmount: Long,
    val amountPerSecond: Double,
    val elapsedSeconds: Long,
    val totalSeconds: Long
)
