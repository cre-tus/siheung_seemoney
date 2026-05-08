package com.siheung.seemoney.domain.budget.dto;

public record BudgetDetailDto(
        Long id,
        Integer year,
        Long categoryId,
        String categoryName,
        String accountType,
        String departmentName,
        String detailName,
        Long amount,
        String note
) {
}

//대분류 하나 눌렀을 때, 그 안에 들어간 부서/회계별 금액을 보여주는 데이터