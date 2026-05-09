package com.siheung.seemoney.domain.budget.dto;

public record BudgetCategorySummaryDto(
        Long categoryId,
        String categoryName,
        Long amount,
        Double ratio,
        Double changeRate,
        String trend
) {
}

//앱 화면에서 대분류 카드 하나에 들어갈 데이터