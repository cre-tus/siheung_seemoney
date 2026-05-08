package com.siheung.seemoney.domain.budget.service;

import com.siheung.seemoney.domain.budget.dto.BudgetCategorySummaryDto;
import com.siheung.seemoney.domain.budget.dto.BudgetDetailDto;
import com.siheung.seemoney.domain.budget.dto.BudgetDetailResponse;
import com.siheung.seemoney.domain.budget.dto.BudgetSummaryResponse;
import com.siheung.seemoney.domain.budget.entity.BudgetItem;
import com.siheung.seemoney.domain.budget.repository.BudgetItemRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import com.siheung.seemoney.domain.budget.dto.BudgetLiveResponse;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetItemRepository budgetItemRepository;

    public BudgetService(BudgetItemRepository budgetItemRepository) {
        this.budgetItemRepository = budgetItemRepository;
    }

    public BudgetSummaryResponse getBudgetSummary(Integer year) {
        List<BudgetItem> currentItems = budgetItemRepository.findByYear(year);
        List<BudgetItem> previousItems = budgetItemRepository.findByYear(year - 1);

        long totalAmount = currentItems.stream()
                .mapToLong(BudgetItem::getAmount)
                .sum();

        List<BudgetCategorySummaryDto> categories = currentItems.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        item -> item.getCategory().getId()
                ))
                .values()
                .stream()
                .map(items -> {
                    BudgetItem first = items.get(0);

                    long categoryAmount = items.stream()
                            .mapToLong(BudgetItem::getAmount)
                            .sum();

                    long previousCategoryAmount = previousItems.stream()
                            .filter(item -> item.getCategory().getId().equals(first.getCategory().getId()))
                            .mapToLong(BudgetItem::getAmount)
                            .sum();

                    double ratio = totalAmount == 0
                            ? 0.0
                            : categoryAmount * 100.0 / totalAmount;

                    Double changeRate = previousCategoryAmount == 0
                            ? null
                            : (categoryAmount - previousCategoryAmount) * 100.0 / previousCategoryAmount;

                    String trend = getTrend(changeRate);

                    return new BudgetCategorySummaryDto(
                            first.getCategory().getId(),
                            first.getCategory().getName(),
                            categoryAmount,
                            roundOneDecimal(ratio),
                            changeRate == null ? null : roundOneDecimal(changeRate),
                            trend
                    );
                })
                .sorted((a, b) -> Long.compare(a.categoryId(), b.categoryId()))
                .toList();

        return new BudgetSummaryResponse(
                year,
                totalAmount,
                categories
        );
    }

    public BudgetDetailResponse getBudgetDetail(Integer year, Long categoryId) {
        List<BudgetItem> items = budgetItemRepository.findByYearAndCategory_Id(year, categoryId);

        long totalAmount = items.stream()
                .mapToLong(BudgetItem::getAmount)
                .sum();

        String categoryName = items.isEmpty()
                ? null
                : items.get(0).getCategory().getName();

        List<BudgetDetailDto> detailItems = items.stream()
                .map(item -> new BudgetDetailDto(
                        item.getId(),
                        item.getYear(),
                        item.getCategory().getId(),
                        item.getCategory().getName(),
                        item.getAccountType(),
                        item.getDepartmentName(),
                        item.getDetailName(),
                        item.getAmount(),
                        item.getNote()
                ))
                .toList();

        return new BudgetDetailResponse(
                year,
                categoryId,
                categoryName,
                totalAmount,
                detailItems
        );
    }

    public BudgetLiveResponse getLiveBudget(Integer year) {
        List<BudgetItem> items = budgetItemRepository.findByYear(year);

        long totalAmount = items.stream()
                .mapToLong(BudgetItem::getAmount)
                .sum();

        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(year + 1, 1, 1, 0, 0, 0);
        LocalDateTime now = LocalDateTime.now();

        long totalSeconds = ChronoUnit.SECONDS.between(start, end);

        long elapsedSeconds;

        if (now.isBefore(start)) {
            elapsedSeconds = 0;
        } else if (now.isAfter(end)) {
            elapsedSeconds = totalSeconds;
        } else {
            elapsedSeconds = ChronoUnit.SECONDS.between(start, now);
        }

        double amountPerSecond = totalAmount / (double) totalSeconds;

        long usedAmount = Math.round(amountPerSecond * elapsedSeconds);
        long remainingAmount = Math.max(totalAmount - usedAmount, 0);

        return new BudgetLiveResponse(
                year,
                totalAmount,
                remainingAmount,
                usedAmount,
                amountPerSecond,
                elapsedSeconds,
                totalSeconds
        );
    }

    private String getTrend(Double changeRate) {
        if (changeRate == null) {
            return "NONE";
        }

        if (changeRate > 0) {
            return "UP";
        }

        if (changeRate < 0) {
            return "DOWN";
        }

        return "SAME";
    }

    private double roundOneDecimal(double value) {
        return Math.round(value * 10) / 10.0;
    }
}

//getBudgetSummary(year) → 8개 대분류 합계, 전체 비율, 전년도 대비 증감률 계산

//getBudgetDetail(year, categoryId) → 특정 대분류 안에 들어간 부서/회계별 예산 목록 반환