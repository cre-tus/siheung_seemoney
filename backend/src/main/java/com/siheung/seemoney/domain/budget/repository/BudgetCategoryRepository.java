package com.siheung.seemoney.domain.budget.repository;

import com.siheung.seemoney.domain.budget.entity.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetCategoryRepository
        extends JpaRepository<BudgetCategory, Long> {
}

//8개 대분류 조회