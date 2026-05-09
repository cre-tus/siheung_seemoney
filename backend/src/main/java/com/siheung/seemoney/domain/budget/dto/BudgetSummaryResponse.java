package com.siheung.seemoney.domain.budget.dto;

import java.util.List;

public record BudgetSummaryResponse(
        Integer year,
        Long totalAmount,
        List<BudgetCategorySummaryDto> categories
) {
}

//앱 화면 전체 응답 데이터