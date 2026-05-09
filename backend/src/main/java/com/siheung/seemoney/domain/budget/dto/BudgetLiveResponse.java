package com.siheung.seemoney.domain.budget.dto;

public record BudgetLiveResponse(
        Integer year,
        Long totalAmount,
        Long remainingAmount,
        Long usedAmount,
        Double amountPerSecond,
        Long elapsedSeconds,
        Long totalSeconds
) {
}