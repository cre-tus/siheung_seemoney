package com.example.siheung_seemoney.data.model

/**
 * 백엔드 /api/debts/realtime/2026 응답 DTO
 * DebtRealtimeCalculator 에서 계산된 실시간 부채 데이터를 수신합니다.
 */
data class DebtRealtimeResponse(
    val year: Int,
    val currentDebt: Long,         // 현재 시점의 부채 (원 단위)
    val startDebt: Long,           // 2026년 1월 1일 기준 부채
    val endDebt: Long,             // 2026년 12월 31일 목표 부채
    val decreasePerSecond: Long,   // 초당 감소량 (원 단위)
    val unit: String               // "원"
)
