package com.siheung.seemoney.domain.debt.dto;

import com.siheung.seemoney.domain.debt.entity.Debt;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DebtResponse {

    private Integer year;
    private Long liabilityAmount;
    private String unit;

    public static DebtResponse from(Debt debt) {
        return DebtResponse.builder()
                .year(debt.getYear())
                .liabilityAmount(debt.getLiabilityAmount())
                .unit(debt.getUnit())
                .build();
    }
}