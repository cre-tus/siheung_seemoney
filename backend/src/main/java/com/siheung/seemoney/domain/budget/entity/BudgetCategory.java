package com.siheung.seemoney.domain.budget.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "budget_category")
public class BudgetCategory {

    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    protected BudgetCategory() {
    }

    public BudgetCategory(Long id, String name, Integer displayOrder) {
        this.id = id;
        this.name = name;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}