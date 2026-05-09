package com.example.siheung_seemoney.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.siheung_seemoney.data.network.RetrofitClient
import com.example.siheung_seemoney.data.network.*
import com.example.siheung_seemoney.data.model.User

/**
 * 인증 관련 Repository
 */
class AuthRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "siheung_seemoney_prefs",
        Context.MODE_PRIVATE
    )

    private val apiService = RetrofitClient.apiService

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 로컬 토큰 관리
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * JWT 토큰 저장
     */
    private fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    /**
     * JWT 토큰 가져오기
     */
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    /**
     * Bearer 토큰 헤더 생성
     */
    fun getBearerToken(): String? {
        return getToken()?.let { "Bearer $it" }
    }

    /**
     * 로그인 여부 확인
     */
    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    /**
     * 사용자 정보 저장
     */
    private fun saveUserInfo(user: User) {
        prefs.edit()
            .putInt(KEY_USER_ID, user.userId)
            .putString(KEY_EMAIL, user.email)
            .apply()
    }

    /**
     * 로그아웃 (토큰 삭제)
     */
    fun logout() {
        prefs.edit().clear().apply()
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // API 호출
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * 회원가입
     */
    suspend fun signup(email: String, password: String, address: String): Result<User> {
        return try {
            val response = apiService.signup(SignupRequest(email, password, address))

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                saveToken(authResponse.token)
                saveUserInfo(authResponse.user)
                Result.success(authResponse.user)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "회원가입 실패"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 로그인
     */
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                saveToken(authResponse.token)
                saveUserInfo(authResponse.user)
                Result.success(authResponse.user)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "로그인 실패"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 내 정보 조회
     */
    suspend fun getMe(): Result<User> {
        val token = getBearerToken() ?: return Result.failure(Exception("로그인이 필요합니다"))

        return try {
            val response = apiService.getMe(token)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.user)
            } else {
                Result.failure(Exception("사용자 정보 조회 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 비밀번호 재설정
     */
    suspend fun resetPassword(email: String, newPassword: String): Result<Boolean> {
        return try {
            val response = apiService.resetPassword(ResetPasswordRequest(email, newPassword))

            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(true)
            } else {
                Result.failure(Exception("비밀번호 재설정 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
