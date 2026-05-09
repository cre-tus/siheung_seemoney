package com.example.siheung_seemoney.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R

class SignupActivity : AppCompatActivity() {

    private var step = 1

    private var showPassword = false
    private var showConfirm = false

    private lateinit var layoutStep1: LinearLayout
    private lateinit var layoutStep2: LinearLayout
    private lateinit var layoutSuccess: LinearLayout

    private lateinit var tvStepText: TextView
    private lateinit var tvError: TextView

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var spinnerDistrict: Spinner
    private lateinit var etAddress: EditText

    private lateinit var tvLength: TextView
    private lateinit var tvNumber: TextView
    private lateinit var tvSpecial: TextView
    private lateinit var tvUpper: TextView

    private lateinit var cbAll: CheckBox
    private lateinit var cbTerms: CheckBox
    private lateinit var cbPrivacy: CheckBox
    private lateinit var cbMarketing: CheckBox

    private lateinit var btnNext: Button
    private lateinit var btnSignup: Button
    private lateinit var btnGoLogin: Button

    private val districts = listOf(
        "대야동", "신천동", "은행동", "신현동", "조남동", "매화동",
        "군자동", "능곡동", "거모동", "배곧동", "정왕동", "목감동", "과림동"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initViews()
        setupSpinner()
        setupCheckbox()
        setupPasswordToggle()
        setupButtons()

        showStep(1)
    }

    private fun initViews() {

        layoutStep1 = findViewById(R.id.layoutStep1)
        layoutStep2 = findViewById(R.id.layoutStep2)
        layoutSuccess = findViewById(R.id.layoutSuccess)

        tvStepText = findViewById(R.id.tvStepText)
        tvError = findViewById(R.id.tvError)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        etAddress = findViewById(R.id.etAddress)

        tvLength = findViewById(R.id.tvLength)
        tvNumber = findViewById(R.id.tvNumber)
        tvSpecial = findViewById(R.id.tvSpecial)
        tvUpper = findViewById(R.id.tvUpper)

        cbAll = findViewById(R.id.cbAll)
        cbTerms = findViewById(R.id.cbTerms)
        cbPrivacy = findViewById(R.id.cbPrivacy)
        cbMarketing = findViewById(R.id.cbMarketing)

        btnNext = findViewById(R.id.btnNext)
        btnSignup = findViewById(R.id.btnSignup)
        btnGoLogin = findViewById(R.id.btnGoLogin)
    }

    private fun setupSpinner() {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("시흥시 동 선택") + districts
        )

        spinnerDistrict.adapter = adapter

        spinnerDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position > 0) {
                        etAddress.setText("경기도 시흥시 ${districts[position - 1]}")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setupCheckbox() {

        cbAll.setOnCheckedChangeListener { _, checked ->
            cbTerms.isChecked = checked
            cbPrivacy.isChecked = checked
            cbMarketing.isChecked = checked
        }
    }

    private fun setupPasswordToggle() {

        val btnShowPassword = findViewById<TextView>(R.id.btnShowPassword)
        val btnShowConfirm = findViewById<TextView>(R.id.btnShowConfirm)

        btnShowPassword.setOnClickListener {

            showPassword = !showPassword

            if (showPassword) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT
                btnShowPassword.text = "숨김"
            } else {
                etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnShowPassword.text = "보기"
            }

            etPassword.setSelection(etPassword.text.length)
        }

        btnShowConfirm.setOnClickListener {

            showConfirm = !showConfirm

            if (showConfirm) {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT
                btnShowConfirm.text = "숨김"
            } else {
                etConfirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnShowConfirm.text = "보기"
            }

            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }
    }

    private fun setupButtons() {

        btnNext.setOnClickListener {

            if (validateStep1()) {
                showStep(2)
            }
        }

        btnSignup.setOnClickListener {

            if (validateStep2()) {
                showStep(3)
            }
        }

        btnGoLogin.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateStep1(): Boolean {

        hideError()

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirm = etConfirmPassword.text.toString()
        val address = etAddress.text.toString().trim()

        updatePasswordValidation(password)

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("올바른 이메일 형식이 아닙니다")
            return false
        }

        if (!isPasswordValid(password)) {
            showError("비밀번호 조건을 모두 충족해주세요")
            return false
        }

        if (password != confirm) {
            showError("비밀번호가 일치하지 않습니다")
            return false
        }

        if (address.isEmpty()) {
            showError("주소를 입력해주세요")
            return false
        }

        return true
    }

    private fun validateStep2(): Boolean {

        hideError()

        if (!cbTerms.isChecked) {
            showError("이용약관 동의는 필수입니다")
            return false
        }

        if (!cbPrivacy.isChecked) {
            showError("개인정보 처리방침 동의는 필수입니다")
            return false
        }

        return true
    }

    private fun updatePasswordValidation(password: String) {

        val length = password.length >= 8
        val number = password.any { it.isDigit() }
        val special = password.any { !it.isLetterOrDigit() }
        val upper = password.any { it.isUpperCase() }

        tvLength.text = if (length) "✓ 8자 이상" else "✗ 8자 이상"
        tvNumber.text = if (number) "✓ 숫자 포함" else "✗ 숫자 포함"
        tvSpecial.text = if (special) "✓ 특수문자 포함" else "✗ 특수문자 포함"
        tvUpper.text = if (upper) "✓ 영문 대문자 포함" else "✗ 영문 대문자 포함"

        val green = 0xFF16A34A.toInt()
        val gray = 0xFF9CA3AF.toInt()

        tvLength.setTextColor(if (length) green else gray)
        tvNumber.setTextColor(if (number) green else gray)
        tvSpecial.setTextColor(if (special) green else gray)
        tvUpper.setTextColor(if (upper) green else gray)
    }

    private fun isPasswordValid(password: String): Boolean {

        return password.length >= 8 &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() } &&
                password.any { it.isUpperCase() }
    }

    private fun showStep(newStep: Int) {

        step = newStep

        layoutStep1.visibility = View.GONE
        layoutStep2.visibility = View.GONE
        layoutSuccess.visibility = View.GONE

        when (step) {

            1 -> {
                layoutStep1.visibility = View.VISIBLE
                tvStepText.text = "1단계: 계정 정보 입력"
            }

            2 -> {
                layoutStep2.visibility = View.VISIBLE
                tvStepText.text = "2단계: 약관 동의"
            }

            3 -> {
                layoutSuccess.visibility = View.VISIBLE
            }
        }
    }

    private fun showError(message: String) {

        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private fun hideError() {

        tvError.visibility = View.GONE
    }
}