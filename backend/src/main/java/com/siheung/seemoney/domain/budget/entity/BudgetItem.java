package com.siheung.seemoney.domain.budget.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "budget_item")
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BudgetCategory category;

    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;

    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;

    @Column(name = "detail_name", columnDefinition = "TEXT")
    private String detailName;

    @Column(nullable = false)
    private Long amount;

    @Column(columnDefinition = "TEXT")
    private String note;

    protected BudgetItem() {
    }

    public Long getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public BudgetCategory getCategory() {
        return category;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDetailName() {
        return detailName;
    }

    public Long getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }
}