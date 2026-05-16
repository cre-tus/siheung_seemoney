package com.example.siheung_seemoney.data.network

import com.google.gson.annotations.SerializedName
import com.example.siheung_seemoney.data.model.User
import com.example.siheung_seemoney.data.model.Activity
import com.example.siheung_seemoney.data.model.ActivityType

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// 요청 (Request) 모델
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * 회원가입 요청
 */
data class SignupRequest(

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("address")
    val address: String
)
/**
 * 로그인 요청
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

/**
 * 비밀번호 재설정 요청
 */
data class ResetPasswordRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("newPassword")
    val newPassword: String
)

/**
 * 활동 추가 요청
 */
data class AddActivityRequest(
    @SerializedName("type")
    val type: ActivityType,

    @SerializedName("title")
    val title: String,

    @SerializedName("points")
    val points: Int,

    @SerializedName("post_id")
    val postId: Int? = null
)

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// 응답 (Response) 모델
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * 인증 성공 응답 (회원가입, 로그인)
 */
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("user")
    val user: User,

    @SerializedName("token")
    val token: String
)

/**
 * 내 정보 조회 응답
 */
data class UserResponse(
    @SerializedName("user")
    val user: User
)

/**
 * 활동 내역 조회 응답
 */
data class ActivitiesResponse(
    @SerializedName("activities")
    val activities: List<Activity>
)

/**
 * 비밀번호 재설정 성공 응답
 */
data class SuccessResponse(
    @SerializedName("success")
    val success: Boolean
)

/**
 * 활동 추가 성공 응답
 */
data class AddActivityResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("id")
    val id: String,

    @SerializedName("date")
    val date: String
)

/**
 * 에러 응답
 */
data class ErrorResponse(
    @SerializedName("error")
    val error: String
)

/**
 * API 헬스 체크 응답
 */
data class HealthResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String
)
