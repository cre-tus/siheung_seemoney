package com.siheung.seemoney.domain.debt.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DebtRealtimeCalculator {

    // 2025년말 부채(원)
    private static final long START_DEBT = 190_300_000_000L;

    // 2026년말 부채(원)
    private static final long END_DEBT = 152_300_000_000L;

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    // 2026년 시작 시점
    private static final ZonedDateTime START_TIME =
            ZonedDateTime.of(
                    2026, 1, 1,
                    0, 0, 0, 0,
                    ZONE_ID
            );

    // 2026년 종료 시점
    private static final ZonedDateTime END_TIME =
            ZonedDateTime.of(
                    2026, 12, 31,
                    23, 59, 59, 0,
                    ZONE_ID
            );

    private DebtRealtimeCalculator() {
    }

    public static long calculateCurrentDebt() {
        ZonedDateTime now = ZonedDateTime.now(ZONE_ID);
        return calculateCurrentDebt(now);
    }

    public static long calculateCurrentDebt(ZonedDateTime now) {
        if (now.isBefore(START_TIME)) {
            return START_DEBT;
        }

        if (now.isAfter(END_TIME)) {
            return END_DEBT;
        }

        long totalSeconds = Duration.between(START_TIME, END_TIME).getSeconds();
        long elapsedSeconds = Duration.between(START_TIME, now).getSeconds();

        double progress = (double) elapsedSeconds / totalSeconds;

        double currentDebt =
                START_DEBT - ((START_DEBT - END_DEBT) * progress);

        return Math.round(currentDebt);
    }

    public static long getStartDebt() {
        return START_DEBT;
    }

    public static long getEndDebt() {
        return END_DEBT;
    }

    public static long getDecreasePerSecond() {
        long totalSeconds = Duration.between(START_TIME, END_TIME).getSeconds();
        return Math.round((double) (START_DEBT - END_DEBT) / totalSeconds);
    }
}