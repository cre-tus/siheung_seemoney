package com.example.siheung_seemoney.data.network

// Retrofit의 HTTP 응답 객체
import retrofit2.Response

// Retrofit 어노테이션들
import retrofit2.http.*

/**
 * 시흥See머니 백엔드 API 인터페이스
 *
 * Retrofit이 이 인터페이스를 기반으로
 * 자동으로 HTTP 통신 코드를 생성함
 */
interface ApiService {

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 인증(Authentication) 관련 API
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * 서버 상태 확인 API
     *
     * GET 요청
     * ex) GET /health
     *
     * 서버가 정상 동작 중인지 체크할 때 사용
     */
    @GET("health")
    suspend fun healthCheck(): Response<HealthResponse>

    /**
     * 회원가입 API
     *
     * POST /auth/signup
     *
     * @Body :
     * request 객체를 JSON 형태로 변환해서 서버에 전송
     *
     * suspend :
     * 코루틴 기반 비동기 함수
     */
    @POST("auth/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<String>

    /**
     * 로그인 API
     *
     * POST /auth/login
     */
    @POST("auth/login")
    suspend fun login(

        // 로그인 요청 데이터(JSON Body)
        @Body request: LoginRequest

    ): Response<AuthResponse>

    /**
     * 내 정보 조회 API
     *
     * GET /auth/me
     *
     * JWT 토큰 필요
     *
     * Authorization 헤더에 토큰 전달
     *
     * ex)
     * Authorization: Bearer eyJhbGci...
     */
    @GET("auth/me")
    suspend fun getMe(

        // HTTP Header에 Authorization 값 추가
        @Header("Authorization") token: String

    ): Response<UserResponse>

    /**
     * 비밀번호 재설정 API
     *
     * POST /auth/reset-password
     */
    @POST("auth/reset-password")
    suspend fun resetPassword(

        // 비밀번호 재설정 요청 데이터
        @Body request: ResetPasswordRequest

    ): Response<SuccessResponse>

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 활동(Activity) 관련 API
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * 활동 내역 조회 API
     *
     * GET /activities
     *
     * JWT 토큰 필요
     */
    @GET("activities")
    suspend fun getActivities(

        // Authorization 헤더에 JWT 토큰 전달
        @Header("Authorization") token: String

    ): Response<ActivitiesResponse>

    /**
     * 활동 추가 API
     *
     * POST /activities
     *
     * JWT 토큰 필요
     */
    @POST("activities")
    suspend fun addActivity(

        // Authorization 헤더에 JWT 토큰 전달
        @Header("Authorization") token: String,

        // 서버로 전송할 활동 데이터
        @Body request: AddActivityRequest

    ): Response<AddActivityResponse>
}