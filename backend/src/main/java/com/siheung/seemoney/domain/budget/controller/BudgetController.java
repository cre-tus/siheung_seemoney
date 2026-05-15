package com.siheung.seemoney.domain.budget.controller;

import com.siheung.seemoney.domain.budget.dto.BudgetDetailResponse;
import com.siheung.seemoney.domain.budget.dto.BudgetSummaryResponse;
import com.siheung.seemoney.domain.budget.service.BudgetService;
import org.springframework.web.bind.annotation.*;
import com.siheung.seemoney.domain.budget.dto.BudgetLiveResponse;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/summary")
    public BudgetSummaryResponse getBudgetSummary(@RequestParam Integer year) {
        return budgetService.getBudgetSummary(year);
    }

    @GetMapping("/categories/{categoryId}")
    public BudgetDetailResponse getBudgetDetail(
            @RequestParam Integer year,
            @PathVariable Long categoryId) {
        return budgetService.getBudgetDetail(year, categoryId);
    }

    @GetMapping("/live")
    public BudgetLiveResponse getLiveBudget(@RequestParam Integer year) {
        return budgetService.getLiveBudget(year);
    }
}

// 이제 앱에서 호출할 주소: GET /api/budgets/summary?year=2025

// 2025년 8개 대분류 요약: GET /api/budgets/categories/1?year=2025

// 라이브 호출 주소: GET http://localhost:8081/api/budgets/live?year=2026