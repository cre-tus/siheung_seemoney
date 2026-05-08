package com.siheung.seemoney.domain.budget.dto;

import java.util.List;

public record BudgetDetailResponse(
        Integer year,
        Long categoryId,
        String categoryName,
        Long totalAmount,
        List<BudgetDetailDto> items
) {
}

//대분류 상세 화면 전체 응답