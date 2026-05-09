package com.example.siheung_seemoney.data.network


import retrofit2.Response
import retrofit2.http.*

/**
 * 시흥See머니 백엔드 API 인터페이스
 */
interface ApiService {

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 인증 API
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * 헬스 체크
     */
    @GET("health")
    suspend fun healthCheck(): Response<HealthResponse>

    /**
     * 회원가입
     */
    @POST("auth/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<AuthResponse>

    /**
     * 로그인
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    /**
     * 내 정보 조회 (JWT 토큰 필요)
     */
    @GET("auth/me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    /**
     * 비밀번호 재설정
     */
    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<SuccessResponse>

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 활동 내역 API
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * 내 활동 내역 조회 (JWT 토큰 필요)
     */
    @GET("activities")
    suspend fun getActivities(
        @Header("Authorization") token: String
    ): Response<ActivitiesResponse>

    /**
     * 활동 추가 (JWT 토큰 필요)
     */
    @POST("activities")
    suspend fun addActivity(
        @Header("Authorization") token: String,
        @Body request: AddActivityRequest
    ): Response<AddActivityResponse>
}
