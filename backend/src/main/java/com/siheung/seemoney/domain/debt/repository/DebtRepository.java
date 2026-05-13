package com.siheung.seemoney.domain.debt.repository;

import com.siheung.seemoney.domain.debt.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DebtRepository extends JpaRepository<Debt, Long> {

    Optional<Debt> findByYear(Integer year);

    List<Debt> findAllByOrderByYearAsc();
}