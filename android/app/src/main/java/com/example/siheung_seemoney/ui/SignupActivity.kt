package com.example.siheung_seemoney.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private var step = 1

    private var showPassword = false
    private var showConfirm = false

    private val districts = listOf(
        "대야동", "신천동", "은행동", "신현동", "조남동", "매화동",
        "군자동", "능곡동", "거모동", "배곧동", "정왕동", "목감동", "과림동"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupCheckbox()
        setupPasswordToggle()
        setupPasswordWatcher()
        setupButtons()

        showStep(1)
    }

    private fun setupSpinner() {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("시흥시 동 선택") + districts
        )

        binding.spinnerDistrict.adapter = adapter

        binding.spinnerDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position > 0) {

                        binding.etAddress.setText(
                            "경기도 시흥시 ${districts[position - 1]}"
                        )
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {}
            }
    }

    private fun setupCheckbox() {

        binding.cbAll.setOnCheckedChangeListener { _, checked ->

            binding.cbTerms.isChecked = checked
            binding.cbPrivacy.isChecked = checked
            binding.cbMarketing.isChecked = checked
        }
    }

    private fun setupPasswordToggle() {

        binding.btnShowPassword.setOnClickListener {

            showPassword = !showPassword

            if (showPassword) {

                binding.etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT

                binding.btnShowPassword.text = "숨김"

            } else {

                binding.etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD

                binding.btnShowPassword.text = "보기"
            }

            binding.etPassword.setSelection(
                binding.etPassword.text.length
            )
        }

        binding.btnShowConfirm.setOnClickListener {

            showConfirm = !showConfirm

            if (showConfirm) {

                binding.etConfirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT

                binding.btnShowConfirm.text = "숨김"

            } else {

                binding.etConfirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD

                binding.btnShowConfirm.text = "보기"
            }

            binding.etConfirmPassword.setSelection(
                binding.etConfirmPassword.text.length
            )
        }
    }

    private fun setupPasswordWatcher() {

        binding.etPassword.addTextChangedListener(

            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    updatePasswordValidation(
                        s.toString()
                    )
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )
    }

    private fun setupButtons() {

        // 회원가입 1단계 -> 로그인 화면

        binding.btnBackLogin.setOnClickListener {

            finish()
        }

        // 회원가입 1단계 -> 회원가입 2단계

        binding.btnNext.setOnClickListener {

            if (validateStep1()) {

                showStep(2)
            }
        }

        // 회원가입 2단계 -> 회원가입 1단계

        binding.btnBack.setOnClickListener {

            showStep(1)
        }

        // 회원가입 완료

        binding.btnSignup.setOnClickListener {

            if (validateStep2()) {

                showStep(3)
            }
        }

        // 로그인 화면 이동

        binding.btnGoLogin.setOnClickListener {

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
    }

    private fun validateStep1(): Boolean {

        hideError()

        val email =
            binding.etEmail.text.toString().trim()

        val password =
            binding.etPassword.text.toString()

        val confirm =
            binding.etConfirmPassword.text.toString()

        val address =
            binding.etAddress.text.toString().trim()

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

        if (!binding.cbTerms.isChecked) {

            showError("이용약관 동의는 필수입니다")
            return false
        }

        if (!binding.cbPrivacy.isChecked) {

            showError("개인정보 처리방침 동의는 필수입니다")
            return false
        }

        return true
    }

    private fun updatePasswordValidation(password: String) {

        val length =
            password.length >= 8

        val number =
            password.any { it.isDigit() }

        val special =
            password.any { !it.isLetterOrDigit() }

        val upper =
            password.any { it.isUpperCase() }

        binding.tvLength.text =
            if (length) "✓ 8자 이상"
            else "✗ 8자 이상"

        binding.tvNumber.text =
            if (number) "✓ 숫자 포함"
            else "✗ 숫자 포함"

        binding.tvSpecial.text =
            if (special) "✓ 특수문자 포함"
            else "✗ 특수문자 포함"

        binding.tvUpper.text =
            if (upper) "✓ 영문 대문자 포함"
            else "✗ 영문 대문자 포함"

        val green = 0xFF16A34A.toInt()
        val gray = 0xFF9CA3AF.toInt()

        binding.tvLength.setTextColor(
            if (length) green else gray
        )

        binding.tvNumber.setTextColor(
            if (number) green else gray
        )

        binding.tvSpecial.setTextColor(
            if (special) green else gray
        )

        binding.tvUpper.setTextColor(
            if (upper) green else gray
        )
    }

    private fun isPasswordValid(password: String): Boolean {

        return password.length >= 8 &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() } &&
                password.any { it.isUpperCase() }
    }

    private fun showStep(newStep: Int) {

        step = newStep

        binding.layoutStep1.visibility = View.GONE
        binding.layoutStep2.visibility = View.GONE
        binding.layoutSuccess.visibility = View.GONE

        when (step) {

            1 -> {

                binding.layoutStep1.visibility =
                    View.VISIBLE

                binding.tvStepText.visibility =
                    View.VISIBLE

                binding.tvStepText.text =
                    "1단계: 계정 정보 입력"
            }

            2 -> {

                binding.layoutStep2.visibility =
                    View.VISIBLE

                binding.tvStepText.visibility =
                    View.VISIBLE

                binding.tvStepText.text =
                    "2단계: 약관 동의"
            }

            3 -> {

                binding.layoutSuccess.visibility =
                    View.VISIBLE

                binding.tvStepText.visibility =
                    View.GONE
            }
        }
    }

    private fun showError(message: String) {

        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    private fun hideError() {

        binding.tvError.visibility = View.GONE
    }
}