package com.example.siheung_seemoney.data.model

/**
 * 분석 화면의 분야별 예산 정보를 담는 데이터 클래스 (DTO).
 * API 연동 시 백엔드 응답 구조에 맞추어 필드명이 변경될 수 있습니다.
 */
data class CategoryBudget(
    val categoryName: String, // 분야명 (예: 복지, 교통)
    val budget: Long,         // 할당액 (원 단위)
    val percentage: Int,      // 전체 예산 대비 비율 (%)
    val changeRate: Double,   // 전년 대비 변동률 (%)
    val colorResId: Int       // UI에서 사용할 프로그레스 바 및 차트 색상 리소스 ID
)
