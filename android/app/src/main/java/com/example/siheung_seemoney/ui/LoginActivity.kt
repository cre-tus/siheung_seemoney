package com.example.siheung_seemoney.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val TAG = "LOGIN_API"

    private var showPassword = false
    private var isLoading = false

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var guestButton: Button
    private lateinit var tvError: TextView
    private lateinit var btnTogglePassword: TextView
    private lateinit var btnForgot: TextView
    private lateinit var btnSignup: TextView

    // =========================
    // 추가된 부분 시작
    // 아이디 저장 + 로그인 유지
    // =========================

    private lateinit var cbRememberId: CheckBox
    private lateinit var cbKeepLogin: CheckBox

    private lateinit var prefs: SharedPreferences

    // =========================
    // 추가된 부분 끝
    // =========================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // =========================
        // SharedPreferences 초기화
        // 로그인 관련 설정 저장소
        // =========================

        prefs = getSharedPreferences(
            "login_pref",
            MODE_PRIVATE
        )

        // =========================
        // 자동 로그인 체크
        // 앱 실행 시 저장된 토큰 확인
        // =========================

        val keepLogin =
            prefs.getBoolean(
                "keep_login",
                false
            )

        val token =
            getSharedPreferences(
                "auth_prefs",
                MODE_PRIVATE
            ).getString(
                "jwt_token",
                null
            )

        Log.d("AUTO_LOGIN", "keepLogin = $keepLogin")
        Log.d("AUTO_LOGIN", "savedToken = $token")

        // 로그인 유지 체크 + 토큰 존재 시
        // 로그인 화면 건너뛰고 홈 이동
        if (keepLogin && token != null) {

            Log.d("AUTO_LOGIN", "자동 로그인 성공")

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
            return
        }

        // =========================
        // 로그인 화면 표시
        // =========================

        setContentView(R.layout.activity_login)

        // =========================
        // View 연결
        // =========================

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        guestButton = findViewById(R.id.guestButton)
        tvError = findViewById(R.id.tvError)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        btnForgot = findViewById(R.id.btnForgot)
        btnSignup = findViewById(R.id.btnSignup)

        cbRememberId = findViewById(R.id.cbRememberId)
        cbKeepLogin = findViewById(R.id.cbKeepLogin)

        // =========================
        // 저장된 이메일 / 체크박스 복원
        // =========================

        loadSavedLoginInfo()

        btnTogglePassword.setOnClickListener {

            showPassword = !showPassword

            if (showPassword) {

                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT

                btnTogglePassword.text = "숨김"

            } else {

                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD

                btnTogglePassword.text = "보기"
            }

            passwordEditText.setSelection(
                passwordEditText.text.length
            )
        }

        loginButton.setOnClickListener {

            handleLogin()
        }

        btnForgot.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ForgotPasswordActivity::class.java
                )
            )
        }

        btnSignup.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SignupActivity::class.java
                )
            )
        }

        guestButton.setOnClickListener {

            goToHome()
        }
    }

    private fun handleLogin() {

        hideError()

        val email =
            emailEditText.text.toString().trim()

        val password =
            passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {

            showError(
                "이메일과 비밀번호를 모두 입력해주세요"
            )

            return
        }

        login(email, password)
    }

    private fun login(
        email: String,
        password: String
    ) {

        setLoading(true)

        val json = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8"
                .toMediaType(),
            json
        )

        val request = Request.Builder()
            .url("http://10.0.2.2:8081/api/auth/login")
            .post(requestBody)
            .build()

        Log.d(TAG, "요청 URL: ${request.url}")
        Log.d(TAG, "요청 Body: $json")

        client.newCall(request)
            .enqueue(object : Callback {

                override fun onFailure(
                    call: Call,
                    e: IOException
                ) {

                    Log.e(TAG, "서버 연결 실패", e)

                    runOnUiThread {

                        setLoading(false)

                        showError(
                            "서버 연결 실패: ${e.message}"
                        )
                    }
                }

                override fun onResponse(
                    call: Call,
                    response: Response
                ) {

                    val result =
                        response.body?.string()

                    Log.d(
                        TAG,
                        "응답 코드: ${response.code}"
                    )

                    Log.d(
                        TAG,
                        "응답 Body: $result"
                    )

                    runOnUiThread {

                        setLoading(false)

                        if (response.isSuccessful) {

                            // =========================
                            // 추가된 부분 시작
                            // 로그인 정보 저장
                            // =========================

                            saveLoginInfo(email)

                            // JWT 토큰 파싱 후 저장 (ParticipateActivity 로그인 체크용)
                            // [현재] 응답 JSON에서 "token" 필드를 간단 파싱
                            // [추후] Retrofit AuthResponse 모델로 교체 권장
                            try {
                                val tokenStart = result?.indexOf("\"accessToken\":") ?: -1
                                if (tokenStart >= 0) {
                                    val valueStart = result!!.indexOf('"', tokenStart + 14) + 1
                                    val valueEnd = result.indexOf('"', valueStart)
                                    val token = result.substring(valueStart, valueEnd)
                                    getSharedPreferences("auth_prefs", MODE_PRIVATE)
                                        .edit()
                                        .putString("jwt_token", token)
                                        .apply()
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "토큰 파싱 실패", e)
                            }

                            // =========================
                            // 추가된 부분 끝
                            // =========================

                            Toast.makeText(
                                this@LoginActivity,
                                "로그인 성공",
                                Toast.LENGTH_SHORT
                            ).show()

                            goToHome()

                        } else {

                            showError(
                                "로그인 실패: $result"
                            )
                        }
                    }
                }
            })
    }

    // =========================
    // 추가된 부분 시작
    // 아이디 저장 / 로그인 유지
    // =========================

    private fun saveLoginInfo(email: String) {

        val editor = prefs.edit()

        if (cbRememberId.isChecked) {

            editor.putString(
                "saved_email",
                email
            )

        } else {

            editor.remove("saved_email")
        }

        editor.putBoolean(
            "keep_login",
            cbKeepLogin.isChecked
        )

        editor.apply()
    }

    private fun loadSavedLoginInfo() {

        val savedEmail =
            prefs.getString(
                "saved_email",
                ""
            )

        val keepLogin =
            prefs.getBoolean(
                "keep_login",
                false
            )

        emailEditText.setText(savedEmail)

        cbRememberId.isChecked =
            savedEmail!!.isNotEmpty()

        cbKeepLogin.isChecked =
            keepLogin
    }

    // =========================
    // 추가된 부분 끝
    // =========================

    private fun setLoading(
        loading: Boolean
    ) {

        isLoading = loading

        loginButton.isEnabled = !loading

        loginButton.text =
            if (loading)
                "로그인 중..."
            else
                "로그인"

        emailEditText.isEnabled =
            !loading

        passwordEditText.isEnabled =
            !loading

        guestButton.isEnabled =
            !loading

        btnForgot.isEnabled =
            !loading

        btnSignup.isEnabled =
            !loading
    }

    private fun goToHome() {

        // 게스트 진입 시 토큰 초기화 (비로그인 상태로 홈 이동)
        val isGuestMode = !cbKeepLogin.isChecked &&
            getSharedPreferences("auth_prefs", MODE_PRIVATE).getString("jwt_token", null) == null
        // 게스트는 토큰 없음 → 참여 탭 접근 시 로그인 유도

        val intent =
            Intent(
                this,
                HomeActivity::class.java
            )

        startActivity(intent)

        finish()
    }

    private fun showError(
        message: String
    ) {

        tvError.text = "⚠ $message"

        tvError.visibility =
            View.VISIBLE
    }

    private fun hideError() {

        tvError.visibility =
            View.GONE
    }
}