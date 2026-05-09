package com.example.siheung_seemoney.ui

import android.content.Intent
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

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var guestButton: Button
    private lateinit var tvError: TextView
    private lateinit var btnTogglePassword: TextView
    private lateinit var btnForgot: TextView
    private lateinit var btnSignup: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        guestButton = findViewById(R.id.guestButton)

        tvError = findViewById(R.id.tvError)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        btnForgot = findViewById(R.id.btnForgot)
        btnSignup = findViewById(R.id.btnSignup)

        btnTogglePassword.setOnClickListener {
            showPassword = !showPassword

            if (showPassword) {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
                btnTogglePassword.text = "숨김"
            } else {
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnTogglePassword.text = "보기"
            }

            passwordEditText.setSelection(passwordEditText.text.length)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            tvError.visibility = View.GONE

            if (email.isEmpty() || password.isEmpty()) {
                showError("이메일과 비밀번호를 모두 입력해주세요")
                return@setOnClickListener
            }

            login(email, password)
        }

        guestButton.setOnClickListener {
            goToHome()
        }

        btnForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun login(email: String, password: String) {
        val json = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            json
        )

        val request = Request.Builder()
            .url("http://10.0.2.2:8081/api/auth/login")
            .post(requestBody)
            .build()

        Log.d(TAG, "요청 URL: ${request.url}")
        Log.d(TAG, "요청 Body: $json")

        loginButton.isEnabled = false
        loginButton.text = "로그인 중..."

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "서버 연결 실패", e)

                runOnUiThread {
                    loginButton.isEnabled = true
                    loginButton.text = "로그인"
                    showError("서버 연결 실패: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()

                Log.d(TAG, "응답 코드: ${response.code}")
                Log.d(TAG, "응답 Body: $result")

                runOnUiThread {
                    loginButton.isEnabled = true
                    loginButton.text = "로그인"

                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        goToHome()
                    } else {
                        showError("로그인 실패: $result")
                    }
                }
            }
        })
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
}