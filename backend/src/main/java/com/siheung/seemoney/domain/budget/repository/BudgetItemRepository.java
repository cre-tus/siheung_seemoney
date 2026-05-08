package com.siheung.seemoney.domain.budget.repository;

import com.siheung.seemoney.domain.budget.entity.BudgetItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetItemRepository
        extends JpaRepository<BudgetItem, Long> {

    List<BudgetItem> findByYear(Integer year);

    List<BudgetItem> findByYearAndCategory_Id(
            Integer year,
            Long categoryId
    );
}

//연도별 예산 조회
//카테고리별 예산 조회