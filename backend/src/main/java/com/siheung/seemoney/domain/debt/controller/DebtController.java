package com.siheung.seemoney.domain.debt.controller;

import com.siheung.seemoney.domain.debt.dto.DebtRealtimeResponse;
import com.siheung.seemoney.domain.debt.dto.DebtResponse;
import com.siheung.seemoney.domain.debt.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtService;

    @GetMapping
    public List<DebtResponse> getAllDebts() {
        return debtService.getAllDebts();
    }

    @GetMapping("/{year}")
    public DebtResponse getDebtByYear(@PathVariable Integer year) {
        return debtService.getDebtByYear(year);
    }

    @GetMapping("/realtime/2026")
    public DebtRealtimeResponse getRealtimeDebt2026() {
        return debtService.getRealtimeDebt2026();
    }
}