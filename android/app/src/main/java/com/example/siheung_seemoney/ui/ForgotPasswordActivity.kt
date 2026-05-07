package com.example.siheung_seemoney.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R
import kotlin.random.Random
class ForgotPasswordActivity : AppCompatActivity() {

    private var generatedCode = ""
    private var step = 1

    private lateinit var tvTitle: TextView
    private lateinit var tvError: TextView
    private lateinit var layoutEmail: LinearLayout
    private lateinit var layoutVerify: LinearLayout
    private lateinit var layoutReset: LinearLayout
    private lateinit var layoutDone: LinearLayout

    private lateinit var etEmail: EditText
    private lateinit var etCode: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var tvDemoCode: TextView

    private lateinit var btnNext: Button
    private lateinit var btnBack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        tvTitle = findViewById(R.id.tvTitle)
        tvError = findViewById(R.id.tvError)
        layoutEmail = findViewById(R.id.layoutEmail)
        layoutVerify = findViewById(R.id.layoutVerify)
        layoutReset = findViewById(R.id.layoutReset)
        layoutDone = findViewById(R.id.layoutDone)

        etEmail = findViewById(R.id.etEmail)
        etCode = findViewById(R.id.etCode)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        tvDemoCode = findViewById(R.id.tvDemoCode)

        btnNext = findViewById(R.id.btnNext)
        btnBack = findViewById(R.id.btnBack)

        showStep(1)

        btnBack.setOnClickListener {
            if (step == 1) {
                finish()
            } else {
                showStep(step - 1)
            }
        }

        btnNext.setOnClickListener {
            when (step) {
                1 -> checkEmail()
                2 -> checkCode()
                3 -> resetPassword()
                4 -> finish()
            }
        }
    }

    private fun checkEmail() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            showError("이메일을 입력해주세요")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("올바른 이메일 형식이 아닙니다")
            return
        }

        generatedCode = Random.nextInt(100000, 999999).toString()
        tvDemoCode.text = generatedCode
        showStep(2)
    }

    private fun checkCode() {
        val code = etCode.text.toString().trim()

        if (code != generatedCode) {
            showError("인증 코드가 올바르지 않습니다")
            return
        }

        showStep(3)
    }

    private fun resetPassword() {
        val newPassword = etNewPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (newPassword.length < 8) {
            showError("비밀번호는 8자 이상이어야 합니다")
            return
        }

        if (newPassword != confirmPassword) {
            showError("비밀번호가 일치하지 않습니다")
            return
        }

        showStep(4)
    }

    private fun showStep(newStep: Int) {
        step = newStep
        tvError.visibility = View.GONE

        layoutEmail.visibility = View.GONE
        layoutVerify.visibility = View.GONE
        layoutReset.visibility = View.GONE
        layoutDone.visibility = View.GONE

        when (step) {
            1 -> {
                tvTitle.text = "이메일 확인"
                layoutEmail.visibility = View.VISIBLE
                btnNext.text = "인증 코드 받기"
            }

            2 -> {
                tvTitle.text = "인증 코드 확인"
                layoutVerify.visibility = View.VISIBLE
                btnNext.text = "확인"
            }

            3 -> {
                tvTitle.text = "새 비밀번호 설정"
                layoutReset.visibility = View.VISIBLE
                btnNext.text = "비밀번호 변경 완료"
            }

            4 -> {
                tvTitle.text = "변경 완료"
                layoutDone.visibility = View.VISIBLE
                btnNext.text = "로그인하러 가기"
            }
        }
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
}