package com.siheung.seemoney.domain.debt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DebtRealtimeResponse {

    private Integer year;
    private Long currentDebt;
    private Long startDebt;
    private Long endDebt;
    private Long decreasePerSecond;
    private String unit;
}