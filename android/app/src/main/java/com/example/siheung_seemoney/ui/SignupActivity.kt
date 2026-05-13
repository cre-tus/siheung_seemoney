package com.example.siheung_seemoney.ui

// Intent : 화면 전환용 클래스
import android.content.Intent

// 액티비티 생명주기 관련 클래스
import android.os.Bundle

// 비밀번호 표시/숨김 inputType 설정용
import android.text.InputType

// 이메일 형식 검사용 정규식 유틸
import android.util.Patterns

// View 관련 클래스
import android.view.View

// 위젯 클래스들
import android.widget.*

// AppCompatActivity 상속
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

// 프로젝트 리소스 접근
import com.example.siheung_seemoney.R
import com.example.siheung_seemoney.data.repository.AuthRepository
import kotlinx.coroutines.launch

// 회원가입 화면 액티비티
class SignupActivity : AppCompatActivity() {

    // 현재 회원가입 단계 저장
    // 1 = 계정 정보 입력
    // 2 = 약관 동의
    // 3 = 가입 완료
    private var step = 1

    // 비밀번호 표시 여부
    private var showPassword = false


    // 비밀번호 확인 표시 여부
    private var showConfirm = false

    // 단계별 레이아웃
    private lateinit var layoutStep1: LinearLayout
    private lateinit var layoutStep2: LinearLayout
    private lateinit var layoutSuccess: LinearLayout

    // 현재 단계 텍스트
    private lateinit var tvStepText: TextView

    // 에러 메시지 출력용
    private lateinit var tvError: TextView

    // 이메일 입력창
    private lateinit var etEmail: EditText

    // 비밀번호 입력창
    private lateinit var etPassword: EditText

    // 비밀번호 확인 입력창
    private lateinit var etConfirmPassword: EditText

    // 시흥시 동 선택 Spinner
    private lateinit var spinnerDistrict: Spinner

    // 주소 입력창
    private lateinit var etAddress: EditText

    // 비밀번호 조건 표시 TextView들
    private lateinit var tvLength: TextView
    private lateinit var tvNumber: TextView
    private lateinit var tvSpecial: TextView
    private lateinit var tvUpper: TextView

    // 전체 동의 체크박스
    private lateinit var cbAll: CheckBox

    // 이용약관 체크박스
    private lateinit var cbTerms: CheckBox

    // 개인정보 체크박스
    private lateinit var cbPrivacy: CheckBox

    // 마케팅 체크박스
    private lateinit var cbMarketing: CheckBox

    // 다음 버튼
    private lateinit var btnNext: Button

    // 회원가입 버튼
    private lateinit var btnSignup: Button

    // 로그인 이동 버튼
    private lateinit var btnGoLogin: Button

    //로그인 리포
    private lateinit var authRepository: AuthRepository

    // Spinner에 표시할 시흥시 동 목록
    private val districts = listOf(
        "대야동", "신천동", "은행동", "신현동", "조남동", "매화동",
        "군자동", "능곡동", "거모동", "배곧동", "정왕동", "목감동", "과림동"
    )

    // 액티비티 생성 시 실행
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // xml 화면 연결
        setContentView(R.layout.activity_signup)

        //로그인 리포 초기화
        authRepository = AuthRepository(this)

        // View 초기화
        initViews()

        // Spinner 설정
        setupSpinner()

        // 체크박스 설정
        setupCheckbox()

        // 비밀번호 보기/숨김 설정
        setupPasswordToggle()

        // 버튼 이벤트 설정
        setupButtons()

        // 첫 번째 단계 화면 표시
        showStep(1)

    }

    // findViewById로 View 연결
    private fun initViews() {

        // 단계별 레이아웃 연결
        layoutStep1 = findViewById(R.id.layoutStep1)
        layoutStep2 = findViewById(R.id.layoutStep2)
        layoutSuccess = findViewById(R.id.layoutSuccess)

        // 단계 텍스트 및 에러 메시지 연결
        tvStepText = findViewById(R.id.tvStepText)
        tvError = findViewById(R.id.tvError)

        // 입력창 연결
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        etAddress = findViewById(R.id.etAddress)

        // 비밀번호 조건 TextView 연결
        tvLength = findViewById(R.id.tvLength)
        tvNumber = findViewById(R.id.tvNumber)
        tvSpecial = findViewById(R.id.tvSpecial)
        tvUpper = findViewById(R.id.tvUpper)

        // 체크박스 연결
        cbAll = findViewById(R.id.cbAll)
        cbTerms = findViewById(R.id.cbTerms)
        cbPrivacy = findViewById(R.id.cbPrivacy)
        cbMarketing = findViewById(R.id.cbMarketing)

        // 버튼 연결
        btnNext = findViewById(R.id.btnNext)
        btnSignup = findViewById(R.id.btnSignup)
        btnGoLogin = findViewById(R.id.btnGoLogin)
    }

    // Spinner 설정 함수
    private fun setupSpinner() {

        // Spinner에 표시할 Adapter 생성
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,

            // 첫 항목은 안내 문구
            listOf("시흥시 동 선택") + districts
        )

        // Spinner에 Adapter 연결
        spinnerDistrict.adapter = adapter

        // Spinner 선택 이벤트
        spinnerDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                // 항목 선택 시 실행
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    // 첫 번째 안내 문구 제외
                    if (position > 0) {

                        // 주소 자동 입력
                        etAddress.setText("경기도 시흥시 ${districts[position - 1]}")
                    }
                }

                // 아무것도 선택 안됐을 때
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    // 체크박스 설정
    private fun setupCheckbox() {

        // 전체 동의 체크 시
        cbAll.setOnCheckedChangeListener { _, checked ->

            // 하위 체크박스 모두 동일하게 변경
            cbTerms.isChecked = checked
            cbPrivacy.isChecked = checked
            cbMarketing.isChecked = checked
        }
    }

    // 비밀번호 보기/숨김 설정
    private fun setupPasswordToggle() {

        // 비밀번호 보기 버튼
        val btnShowPassword = findViewById<TextView>(R.id.btnShowPassword)

        // 비밀번호 확인 보기 버튼
        val btnShowConfirm = findViewById<TextView>(R.id.btnShowConfirm)

        // 비밀번호 보기 버튼 클릭
        btnShowPassword.setOnClickListener {

            // true/false 반전
            showPassword = !showPassword

            if (showPassword) {

                // 일반 텍스트로 표시
                etPassword.inputType = InputType.TYPE_CLASS_TEXT

                btnShowPassword.text = "숨김"

            } else {

                // 비밀번호 형태로 표시
                etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                btnShowPassword.text = "보기"
            }

            // 커서를 마지막으로 이동
            etPassword.setSelection(etPassword.text.length)
        }

        // 비밀번호 확인 보기 버튼 클릭
        btnShowConfirm.setOnClickListener {

            // true/false 반전
            showConfirm = !showConfirm

            if (showConfirm) {

                // 일반 텍스트 표시
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT

                btnShowConfirm.text = "숨김"

            } else {

                // 비밀번호 형태 표시
                etConfirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                btnShowConfirm.text = "보기"
            }

            // 커서를 마지막으로 이동
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }
    }

    // 버튼 이벤트 설정
    private fun setupButtons() {

        // 다음 버튼 클릭
        btnNext.setOnClickListener {

            // 1단계 검증 성공 시
            if (validateStep1()) {

                // 2단계 화면 표시
                showStep(2)
            }
        }

        // 회원가입 버튼 클릭
        btnSignup.setOnClickListener {

            if (validateStep2()) {

                lifecycleScope.launch {

                    val result = authRepository.signup(
                        email = etEmail.text.toString().trim(),
                        password = etPassword.text.toString(),
                        address = etAddress.text.toString().trim()
                    )

                    if (result.isSuccess) {
                        showStep(3)
                    } else {
                        showError(result.exceptionOrNull()?.message ?: "회원가입 실패")
                    }
                }
            }
        }

        // 로그인 이동 버튼 클릭
        btnGoLogin.setOnClickListener {

            // 로그인 화면 이동
            startActivity(Intent(this, LoginActivity::class.java))

            // 현재 액티비티 종료
            finish()
        }
    }

    // 1단계 입력 검증
    private fun validateStep1(): Boolean {

        // 기존 에러 숨김
        hideError()

        // 이메일 입력값
        val email = etEmail.text.toString().trim()

        // 비밀번호 입력값
        val password = etPassword.text.toString()

        // 비밀번호 확인 입력값
        val confirm = etConfirmPassword.text.toString()

        // 주소 입력값
        val address = etAddress.text.toString().trim()

        // 비밀번호 조건 표시 업데이트
        updatePasswordValidation(password)

        // 이메일 형식 검사
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            showError("올바른 이메일 형식이 아닙니다")
            return false
        }

        // 비밀번호 조건 검사
        if (!isPasswordValid(password)) {

            showError("비밀번호 조건을 모두 충족해주세요")
            return false
        }

        // 비밀번호 일치 검사
        if (password != confirm) {

            showError("비밀번호가 일치하지 않습니다")
            return false
        }

        // 주소 입력 여부 검사
        if (address.isEmpty()) {

            showError("주소를 입력해주세요")
            return false
        }

        // 모든 조건 통과
        return true
    }

    // 2단계 약관 검사
    private fun validateStep2(): Boolean {

        hideError()

        // 이용약관 필수 체크
        if (!cbTerms.isChecked) {

            showError("이용약관 동의는 필수입니다")
            return false
        }

        // 개인정보 동의 필수 체크
        if (!cbPrivacy.isChecked) {

            showError("개인정보 처리방침 동의는 필수입니다")
            return false
        }

        return true
    }

    // 비밀번호 조건 표시 업데이트
    private fun updatePasswordValidation(password: String) {

        // 8자 이상 여부
        val length = password.length >= 8

        // 숫자 포함 여부
        val number = password.any { it.isDigit() }

        // 특수문자 포함 여부
        val special = password.any { !it.isLetterOrDigit() }

        // 대문자 포함 여부
        val upper = password.any { it.isUpperCase() }

        // 조건 만족 여부 표시
        tvLength.text = if (length) "✓ 8자 이상" else "✗ 8자 이상"
        tvNumber.text = if (number) "✓ 숫자 포함" else "✗ 숫자 포함"
        tvSpecial.text = if (special) "✓ 특수문자 포함" else "✗ 특수문자 포함"
        tvUpper.text = if (upper) "✓ 영문 대문자 포함" else "✗ 영문 대문자 포함"

        // 색상 값
        val green = 0xFF16A34A.toInt()
        val gray = 0xFF9CA3AF.toInt()

        // 조건 만족 시 초록색
        tvLength.setTextColor(if (length) green else gray)
        tvNumber.setTextColor(if (number) green else gray)
        tvSpecial.setTextColor(if (special) green else gray)
        tvUpper.setTextColor(if (upper) green else gray)
    }

    // 비밀번호 유효성 검사 함수
    private fun isPasswordValid(password: String): Boolean {

        return password.length >= 8 &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() } &&
                password.any { it.isUpperCase() }
    }

    // 단계 화면 변경
    private fun showStep(newStep: Int) {

        // 현재 단계 저장
        step = newStep

        // 모든 레이아웃 숨김
        layoutStep1.visibility = View.GONE
        layoutStep2.visibility = View.GONE
        layoutSuccess.visibility = View.GONE

        // 단계별 화면 표시
        when (step) {

            // 1단계
            1 -> {

                layoutStep1.visibility = View.VISIBLE
                tvStepText.text = "1단계: 계정 정보 입력"
            }

            // 2단계
            2 -> {

                layoutStep2.visibility = View.VISIBLE
                tvStepText.text = "2단계: 약관 동의"
            }

            // 완료 화면
            3 -> {

                layoutSuccess.visibility = View.VISIBLE
            }
        }
    }

    // 에러 메시지 출력
    private fun showError(message: String) {

        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    // 에러 메시지 숨김
    private fun hideError() {

        tvError.visibility = View.GONE
    }
}