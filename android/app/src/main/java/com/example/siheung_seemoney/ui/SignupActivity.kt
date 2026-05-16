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
import androidx.lifecycle.lifecycleScope
import com.example.siheung_seemoney.data.repository.AuthRepository
import com.example.siheung_seemoney.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivitySignupBinding

    // 회원가입 Repository
    private lateinit var authRepository: AuthRepository

    // 현재 단계
    private var step = 1

    // 비밀번호 표시 여부
    private var showPassword = false
    private var showConfirm = false

    // 시흥시 동 목록
    private val districts = listOf(
        "대야동", "신천동", "은행동", "신현동", "조남동", "매화동",
        "군자동", "능곡동", "거모동", "배곧동", "정왕동", "목감동", "과림동"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 연결
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Repository 초기화
        authRepository = AuthRepository(this)

        setupSpinner()
        setupCheckbox()
        setupPasswordToggle()
        setupPasswordWatcher()
        setupButtons()

        showStep(1)
    }

    // Spinner 설정
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

    // 체크박스 설정
    private fun setupCheckbox() {

        binding.cbAll.setOnCheckedChangeListener { _, checked ->

            binding.cbTerms.isChecked = checked
            binding.cbPrivacy.isChecked = checked
            binding.cbMarketing.isChecked = checked
        }
    }

    // 비밀번호 보기/숨김
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

    // 실시간 비밀번호 조건 체크
    private fun setupPasswordWatcher() {

        binding.etPassword.addTextChangedListener(

            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

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
                ) {}
            }
        )
    }

    // 버튼 이벤트
    private fun setupButtons() {

        // 로그인 화면으로 돌아가기
        binding.btnBackLogin.setOnClickListener {

            finish()
        }

        // STEP1 -> STEP2
        binding.btnNext.setOnClickListener {

            if (validateStep1()) {

                showStep(2)
            }
        }

        // STEP2 -> STEP1
        binding.btnBack.setOnClickListener {

            showStep(1)
        }

        // 회원가입 완료
        binding.btnSignup.setOnClickListener {

            if (validateStep2()) {

                lifecycleScope.launch {

                    val result = authRepository.signup(
                        email = binding.etEmail.text.toString().trim(),
                        password = binding.etPassword.text.toString(),
                        nickname = binding.etNickname.text.toString().trim(),
                        address = binding.etAddress.text.toString().trim()
                    )

                    if (result.isSuccess) {

                        showStep(3)

                    } else {

                        showError(
                            result.exceptionOrNull()?.message
                                ?: "회원가입 실패"
                        )
                    }
                }
            }
        }

        // 로그인 화면 이동
        binding.btnGoLogin.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }
    }

    // STEP1 검증
    private fun validateStep1(): Boolean {

        hideError()

        val email =
            binding.etEmail.text.toString().trim()

        val password =
            binding.etPassword.text.toString()

        val confirm =
            binding.etConfirmPassword.text.toString()

        val nickname =
            binding.etNickname.text.toString().trim()

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

        if (nickname.isEmpty()) {

            showError("닉네임을 입력해주세요")
            return false
        }

        if (nickname.length < 2) {

            showError("닉네임은 2자 이상이어야 합니다")
            return false
        }

        if (address.isEmpty()) {

            showError("주소를 입력해주세요")
            return false
        }

        return true
    }

    // STEP2 검증
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

    // 비밀번호 조건 표시
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

    // 비밀번호 유효성
    private fun isPasswordValid(password: String): Boolean {

        return password.length >= 8 &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() } &&
                password.any { it.isUpperCase() }
    }

    // 단계 변경
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

    // 에러 출력
    private fun showError(message: String) {

        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    // 에러 숨김
    private fun hideError() {

        binding.tvError.visibility = View.GONE
    }
}