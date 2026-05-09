package com.example.siheung_seemoney.data.model

import com.google.gson.annotations.SerializedName

/**
 * 사용자 등급
 */
enum class UserGrade {
    @SerializedName("BRONZE")
    BRONZE,

    @SerializedName("SILVER")
    SILVER,

    @SerializedName("GOLD")
    GOLD,

    @SerializedName("PLATINUM")
    PLATINUM,

    @SerializedName("DIAMOND")
    DIAMOND
}

/**
 * 사용자 역할
 */
enum class UserRole {
    @SerializedName("USER")
    USER,

    @SerializedName("ADMIN")
    ADMIN
}

/**
 * 사용자 정보 (DB users 테이블)
 */
data class User(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("point")
    val point: Int = 0,

    @SerializedName("user_grade")
    val userGrade: UserGrade = UserGrade.BRONZE,

    @SerializedName("vote_count")
    val voteCount: Int = 0,

    @SerializedName("proposal_count")
    val proposalCount: Int = 0,

    @SerializedName("ranking_point")
    val rankingPoint: Int = 0,

    @SerializedName("role")
    val role: UserRole = UserRole.USER,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

/**
 * 등급별 한글 이름
 */
fun UserGrade.toKorean(): String = when (this) {
    UserGrade.BRONZE -> "브론즈"
    UserGrade.SILVER -> "실버"
    UserGrade.GOLD -> "골드"
    UserGrade.PLATINUM -> "플래티넘"
    UserGrade.DIAMOND -> "다이아몬드"
}

/**
 * 등급별 색상 코드
 */
fun UserGrade.toColor(): String = when (this) {
    UserGrade.BRONZE -> "#CD7F32"
    UserGrade.SILVER -> "#A8A9AD"
    UserGrade.GOLD -> "#FFD700"
    UserGrade.PLATINUM -> "#E5E4E2"
    UserGrade.DIAMOND -> "#B9F2FF"
}
