package com.siheung.seemoney.domain.debt.service;

import com.siheung.seemoney.domain.debt.dto.DebtRealtimeResponse;
import com.siheung.seemoney.domain.debt.dto.DebtResponse;
import com.siheung.seemoney.domain.debt.entity.Debt;
import com.siheung.seemoney.domain.debt.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtService {

    private final DebtRepository debtRepository;

    public List<DebtResponse> getAllDebts() {
        return debtRepository.findAllByOrderByYearAsc()
                .stream()
                .map(DebtResponse::from)
                .toList();
    }

    public DebtResponse getDebtByYear(Integer year) {
        Debt debt = debtRepository.findByYear(year)
                .orElseThrow(() -> new IllegalArgumentException("해당 연도의 부채 데이터가 없습니다. year=" + year));

        return DebtResponse.from(debt);
    }
    public DebtRealtimeResponse getRealtimeDebt2026() {
        return DebtRealtimeResponse.builder()
                .year(2026)
                .currentDebt(DebtRealtimeCalculator.calculateCurrentDebt())
                .startDebt(DebtRealtimeCalculator.getStartDebt())
                .endDebt(DebtRealtimeCalculator.getEndDebt())
                .decreasePerSecond(DebtRealtimeCalculator.getDecreasePerSecond())
                .unit("원")
                .build();
    }
}