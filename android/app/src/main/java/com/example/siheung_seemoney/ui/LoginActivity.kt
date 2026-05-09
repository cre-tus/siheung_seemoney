package com.example.siheung_seemoney.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R

class LoginActivity : AppCompatActivity() {

    private var showPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val guestButton = findViewById<Button>(R.id.guestButton)
        val tvError = findViewById<TextView>(R.id.tvError)
        val btnTogglePassword = findViewById<TextView>(R.id.btnTogglePassword)
        val btnForgot = findViewById<TextView>(R.id.btnForgot)
        val btnSignup = findViewById<TextView>(R.id.btnSignup)

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
                tvError.text = "이메일과 비밀번호를 모두 입력해주세요"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        guestButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        btnForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}