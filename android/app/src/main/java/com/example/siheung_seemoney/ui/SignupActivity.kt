package com.example.siheung_seemoney.ui

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R

class SignupActivity : AppCompatActivity() {

    private var step = 1

    private lateinit var layoutStep1: LinearLayout
    private lateinit var layoutStep2: LinearLayout
    private lateinit var layoutSuccess: LinearLayout

    private lateinit var tvStepText: TextView
    private lateinit var tvError: TextView

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etAddress: EditText

    private lateinit var cbAll: CheckBox
    private lateinit var cbTerms: CheckBox
    private lateinit var cbPrivacy: CheckBox
    private lateinit var cbMarketing: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        layoutStep1 = findViewById(R.id.layoutStep1)
        layoutStep2 = findViewById(R.id.layoutStep2)
        layoutSuccess = findViewById(R.id.layoutSuccess)

        tvStepText = findViewById(R.id.tvStepText)
        tvError = findViewById(R.id.tvError)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etAddress = findViewById(R.id.etAddress)

        cbAll = findViewById(R.id.cbAll)
        cbTerms = findViewById(R.id.cbTerms)
        cbPrivacy = findViewById(R.id.cbPrivacy)
        cbMarketing = findViewById(R.id.cbMarketing)

        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val btnGoLogin = findViewById<Button>(R.id.btnGoLogin)

        cbAll.setOnCheckedChangeListener { _, checked ->
            cbTerms.isChecked = checked
            cbPrivacy.isChecked = checked
            cbMarketing.isChecked = checked
        }

        btnNext.setOnClickListener {
            if (validateStep1()) {
                showStep2()
            }
        }

        btnSignup.setOnClickListener {
            if (validateStep2()) {
                showSuccess()
            }
        }

        btnGoLogin.setOnClickListener {
            finish()
        }

        showStep1()
    }

    private fun validateStep1(): Boolean {

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirm = etConfirmPassword.text.toString().trim()
        val address = etAddress.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return showError("올바른 이메일 형식이 아닙니다")
        }

        if (password.length < 8) {
            return showError("비밀번호는 8자 이상이어야 합니다")
        }

        if (password != confirm) {
            return showError("비밀번호가 일치하지 않습니다")
        }

        if (address.isEmpty()) {
            return showError("주소를 입력해주세요")
        }

        tvError.visibility = View.GONE
        return true
    }

    private fun validateStep2(): Boolean {

        if (!cbTerms.isChecked) {
            return showError("이용약관 동의는 필수입니다")
        }

        if (!cbPrivacy.isChecked) {
            return showError("개인정보 처리방침 동의는 필수입니다")
        }

        return true
    }

    private fun showStep1() {
        step = 1

        layoutStep1.visibility = View.VISIBLE
        layoutStep2.visibility = View.GONE
        layoutSuccess.visibility = View.GONE

        tvStepText.text = "1단계: 계정 정보 입력"
    }

    private fun showStep2() {
        step = 2

        layoutStep1.visibility = View.GONE
        layoutStep2.visibility = View.VISIBLE
        layoutSuccess.visibility = View.GONE

        tvStepText.text = "2단계: 약관 동의"
    }

    private fun showSuccess() {

        layoutStep1.visibility = View.GONE
        layoutStep2.visibility = View.GONE
        layoutSuccess.visibility = View.VISIBLE
    }

    private fun showError(message: String): Boolean {

        tvError.text = message
        tvError.visibility = View.VISIBLE

        return false
    }
}