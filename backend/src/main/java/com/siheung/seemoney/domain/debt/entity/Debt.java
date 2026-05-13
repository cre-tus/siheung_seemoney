package com.siheung.seemoney.domain.debt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "debts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연도
    @Column(nullable = false, unique = true)
    private Integer year;

    // 부채금액 (단위: 원)
    @Column(name = "liability_amount", nullable = false)
    private Long liabilityAmount;

    // 단위
    @Column(nullable = false, length = 20)
    private String unit;
}