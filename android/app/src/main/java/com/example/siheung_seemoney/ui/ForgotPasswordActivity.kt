package com.example.siheung_seemoney.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ForgotPasswordActivity : AppCompatActivity() {

    private var step = "email"
    private var generatedCode = ""
    private var codeTimer: CountDownTimer? = null
    private var showPassword = false
    private var showConfirm = false
    private var updatedAt = ""

    private lateinit var tvMainTitle: TextView
    private lateinit var tvStepTitle: TextView
    private lateinit var tvStepDescription: TextView
    private lateinit var tvError: TextView
    private lateinit var tvDemoCode: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvDoneEmail: TextView
    private lateinit var tvDoneUpdatedAt: TextView

    private lateinit var layoutEmail: LinearLayout
    private lateinit var layoutVerify: LinearLayout
    private lateinit var layoutReset: LinearLayout
    private lateinit var layoutDone: LinearLayout

    private lateinit var bar1: View
    private lateinit var bar2: View
    private lateinit var bar3: View

    private lateinit var etEmail: EditText
    private lateinit var etVerifyCode: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText

    private lateinit var tvLength: TextView
    private lateinit var tvNumber: TextView
    private lateinit var tvSpecial: TextView
    private lateinit var tvUpper: TextView
    private lateinit var tvPasswordMatch: TextView

    private lateinit var btnBack: TextView
    private lateinit var btnMain: Button
    private lateinit var btnResend: TextView
    private lateinit var btnShowPassword: TextView
    private lateinit var btnShowConfirm: TextView
    private lateinit var btnLoginBottom: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        bindViews()
        setupEvents()
        showStep("email")
    }

    private fun bindViews() {
        tvMainTitle = findViewById(R.id.tvMainTitle)
        tvStepTitle = findViewById(R.id.tvStepTitle)
        tvStepDescription = findViewById(R.id.tvStepDescription)
        tvError = findViewById(R.id.tvError)
        tvDemoCode = findViewById(R.id.tvDemoCode)
        tvTimer = findViewById(R.id.tvTimer)
        tvDoneEmail = findViewById(R.id.tvDoneEmail)
        tvDoneUpdatedAt = findViewById(R.id.tvDoneUpdatedAt)

        layoutEmail = findViewById(R.id.layoutEmail)
        layoutVerify = findViewById(R.id.layoutVerify)
        layoutReset = findViewById(R.id.layoutReset)
        layoutDone = findViewById(R.id.layoutDone)

        bar1 = findViewById(R.id.bar1)
        bar2 = findViewById(R.id.bar2)
        bar3 = findViewById(R.id.bar3)

        etEmail = findViewById(R.id.etEmail)
        etVerifyCode = findViewById(R.id.etVerifyCode)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        tvLength = findViewById(R.id.tvLength)
        tvNumber = findViewById(R.id.tvNumber)
        tvSpecial = findViewById(R.id.tvSpecial)
        tvUpper = findViewById(R.id.tvUpper)
        tvPasswordMatch = findViewById(R.id.tvPasswordMatch)

        btnBack = findViewById(R.id.btnBack)
        btnMain = findViewById(R.id.btnMain)
        btnResend = findViewById(R.id.btnResend)
        btnShowPassword = findViewById(R.id.btnShowPassword)
        btnShowConfirm = findViewById(R.id.btnShowConfirm)
        btnLoginBottom = findViewById(R.id.btnLoginBottom)
    }

    private fun setupEvents() {
        btnBack.setOnClickListener {
            when (step) {
                "email" -> goLogin()
                "verify" -> showStep("email")
                "reset" -> showStep("verify")
                "done" -> goLogin()
            }
        }

        btnMain.setOnClickListener {
            when (step) {
                "email" -> handleEmailSubmit()
                "verify" -> handleVerifySubmit()
                "reset" -> handleResetSubmit()
                "done" -> goLogin()
            }
        }

        btnResend.setOnClickListener {
            handleResendCode()
        }

        btnShowPassword.setOnClickListener {
            showPassword = !showPassword
            if (showPassword) {
                etNewPassword.inputType = InputType.TYPE_CLASS_TEXT
                btnShowPassword.text = "숨김"
            } else {
                etNewPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnShowPassword.text = "보기"
            }
            etNewPassword.setSelection(etNewPassword.text.length)
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

        btnLoginBottom.setOnClickListener {
            goLogin()
        }

        etNewPassword.setOnFocusChangeListener { _, _ ->
            updatePasswordValidation()
        }

        etNewPassword.setOnKeyListener { _, _, _ ->
            updatePasswordValidation()
            false
        }

        etConfirmPassword.setOnKeyListener { _, _, _ ->
            updatePasswordMatch()
            false
        }
    }

    private fun handleEmailSubmit() {
        hideError()

        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            showError("이메일을 입력해주세요")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("올바른 이메일 형식이 아닙니다")
            return
        }

        setLoading(true)

        btnMain.postDelayed({
            generatedCode = Random.nextInt(100000, 999999).toString()
            tvDemoCode.text = generatedCode
            setLoading(false)
            showStep("verify")
            startTimer()
        }, 1200)
    }

    private fun handleResendCode() {
        generatedCode = Random.nextInt(100000, 999999).toString()
        tvDemoCode.text = generatedCode
        etVerifyCode.text.clear()
        hideError()
        startTimer()
    }

    private fun handleVerifySubmit() {
        hideError()

        val code = etVerifyCode.text.toString().trim()

        if (tvTimer.text.toString() == "00:00") {
            showError("인증 코드가 만료되었습니다. 다시 요청해주세요")
            return
        }

        if (code != generatedCode) {
            showError("인증 코드가 올바르지 않습니다")
            return
        }

        showStep("reset")
    }

    private fun handleResetSubmit() {
        hideError()

        val newPassword = etNewPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        updatePasswordValidation()
        updatePasswordMatch()

        if (!isPasswordValid(newPassword)) {
            showError("비밀번호 조건을 모두 충족해주세요")
            return
        }

        if (newPassword != confirmPassword) {
            showError("비밀번호가 일치하지 않습니다")
            return
        }

        updatedAt = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss", Locale.KOREA).format(Date())
        tvDoneEmail.text = etEmail.text.toString().trim()
        tvDoneUpdatedAt.text = updatedAt

        showStep("done")
    }

    private fun showStep(nextStep: String) {
        step = nextStep
        hideError()

        layoutEmail.visibility = View.GONE
        layoutVerify.visibility = View.GONE
        layoutReset.visibility = View.GONE
        layoutDone.visibility = View.GONE

        btnResend.visibility = View.GONE
        btnLoginBottom.visibility = if (step == "done") View.GONE else View.VISIBLE

        when (step) {
            "email" -> {
                tvStepTitle.text = "이메일 확인"
                tvStepDescription.text = "가입 시 사용한 이메일을 입력해주세요"
                btnMain.text = "인증 코드 받기"
                layoutEmail.visibility = View.VISIBLE
                updateProgress(1)
            }

            "verify" -> {
                tvStepTitle.text = "인증 코드 확인"
                tvStepDescription.text = "${etEmail.text.toString().trim()}로 발송된 코드를 입력해주세요"
                btnMain.text = "확인"
                btnResend.visibility = View.VISIBLE
                layoutVerify.visibility = View.VISIBLE
                updateProgress(2)
            }

            "reset" -> {
                tvStepTitle.text = "새 비밀번호 설정"
                tvStepDescription.text = "새로운 비밀번호를 설정해주세요"
                btnMain.text = "비밀번호 변경 완료"
                layoutReset.visibility = View.VISIBLE
                updateProgress(3)
            }

            "done" -> {
                tvStepTitle.text = "변경 완료"
                tvStepDescription.text = "새 비밀번호로 성공적으로 변경되었습니다."
                btnMain.text = "로그인하러 가기"
                layoutDone.visibility = View.VISIBLE
                updateProgress(3)
            }
        }
    }

    private fun updateProgress(num: Int) {
        val active = 0xFFFFFFFF.toInt()
        val inactive = 0x55FFFFFF

        bar1.setBackgroundColor(if (num >= 1) active else inactive)
        bar2.setBackgroundColor(if (num >= 2) active else inactive)
        bar3.setBackgroundColor(if (num >= 3) active else inactive)
    }

    private fun startTimer() {
        codeTimer?.cancel()

        codeTimer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val total = (millisUntilFinished / 1000).toInt()
                val m = total / 60
                val s = total % 60
                tvTimer.text = "%02d:%02d".format(m, s)
            }

            override fun onFinish() {
                tvTimer.text = "00:00"
            }
        }

        codeTimer?.start()
    }

    private fun setLoading(loading: Boolean) {
        btnMain.isEnabled = !loading
        btnMain.text = if (loading) "이메일 확인 중..." else "인증 코드 받기"
    }

    private fun updatePasswordValidation() {
        val password = etNewPassword.text.toString()

        val length = password.length >= 8
        val number = password.any { it.isDigit() }
        val special = password.any { !it.isLetterOrDigit() }
        val upper = password.any { it.isUpperCase() }

        setValidText(tvLength, length, "8자 이상")
        setValidText(tvNumber, number, "숫자 포함")
        setValidText(tvSpecial, special, "특수문자 포함")
        setValidText(tvUpper, upper, "영문 대문자 포함")
    }

    private fun updatePasswordMatch() {
        val newPassword = etNewPassword.text.toString()
        val confirm = etConfirmPassword.text.toString()

        if (confirm.isNotEmpty() && newPassword == confirm) {
            tvPasswordMatch.text = "✓ 비밀번호가 일치합니다"
            tvPasswordMatch.setTextColor(0xFF16A34A.toInt())
            tvPasswordMatch.visibility = View.VISIBLE
        } else if (confirm.isNotEmpty()) {
            tvPasswordMatch.text = "비밀번호가 일치하지 않습니다"
            tvPasswordMatch.setTextColor(0xFFDC2626.toInt())
            tvPasswordMatch.visibility = View.VISIBLE
        } else {
            tvPasswordMatch.visibility = View.GONE
        }
    }

    private fun setValidText(view: TextView, valid: Boolean, label: String) {
        view.text = if (valid) "✓ $label" else "○ $label"
        view.setTextColor(if (valid) 0xFF16A34A.toInt() else 0xFF9CA3AF.toInt())
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() } &&
                password.any { it.isUpperCase() }
    }

    private fun showError(message: String) {
        tvError.text = "⚠ $message"
        tvError.visibility = View.VISIBLE
    }

    private fun hideError() {
        tvError.visibility = View.GONE
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        codeTimer?.cancel()
    }
}