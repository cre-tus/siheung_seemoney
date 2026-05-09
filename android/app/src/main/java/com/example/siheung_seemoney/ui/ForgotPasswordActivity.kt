package com.example.siheung_seemoney.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R
import kotlin.random.Random

class ForgotPasswordActivity : AppCompatActivity() {

    private var step = 1
    private var generatedCode = ""
    private var timer: CountDownTimer? = null

    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var tvError: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvDemoCode: TextView

    private lateinit var layoutEmail: LinearLayout
    private lateinit var layoutVerify: LinearLayout
    private lateinit var layoutReset: LinearLayout
    private lateinit var layoutDone: LinearLayout

    private lateinit var etEmail: EditText
    private lateinit var etCode: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText

    private lateinit var btnBack: TextView
    private lateinit var btnNext: Button
    private lateinit var btnResend: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        tvTitle = findViewById(R.id.tvTitle)
        tvSubtitle = findViewById(R.id.tvSubtitle)
        tvError = findViewById(R.id.tvError)
        tvTimer = findViewById(R.id.tvTimer)
        tvDemoCode = findViewById(R.id.tvDemoCode)

        layoutEmail = findViewById(R.id.layoutEmail)
        layoutVerify = findViewById(R.id.layoutVerify)
        layoutReset = findViewById(R.id.layoutReset)
        layoutDone = findViewById(R.id.layoutDone)

        etEmail = findViewById(R.id.etEmail)
        etCode = findViewById(R.id.etCode)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        btnBack = findViewById(R.id.btnBack)
        btnNext = findViewById(R.id.btnNext)
        btnResend = findViewById(R.id.btnResend)

        btnBack.setOnClickListener {
            when (step) {
                1 -> finish()
                2 -> showStep(1)
                3 -> showStep(2)
                4 -> finish()
            }
        }

        btnNext.setOnClickListener {
            when (step) {
                1 -> handleEmail()
                2 -> handleVerify()
                3 -> handleReset()
                4 -> finish()
            }
        }

        btnResend.setOnClickListener {
            generateCode()
            startTimer()
            etCode.text.clear()
            hideError()
        }

        showStep(1)
    }

    private fun handleEmail() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            showError("이메일을 입력해주세요")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("올바른 이메일 형식이 아닙니다")
            return
        }

        generateCode()
        startTimer()
        showStep(2)
    }

    private fun handleVerify() {
        val code = etCode.text.toString().trim()

        if (code.length < 6) {
            showError("인증 코드 6자리를 입력해주세요")
            return
        }

        if (code != generatedCode) {
            showError("인증 코드가 올바르지 않습니다")
            return
        }

        showStep(3)
    }

    private fun handleReset() {
        val password = etNewPassword.text.toString().trim()
        val confirm = etConfirmPassword.text.toString().trim()

        if (password.length < 8) {
            showError("비밀번호는 8자 이상이어야 합니다")
            return
        }

        if (!password.any { it.isDigit() }) {
            showError("비밀번호에 숫자를 포함해주세요")
            return
        }

        if (!password.any { it.isUpperCase() }) {
            showError("비밀번호에 영문 대문자를 포함해주세요")
            return
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            showError("비밀번호에 특수문자를 포함해주세요")
            return
        }

        if (password != confirm) {
            showError("비밀번호가 일치하지 않습니다")
            return
        }

        showStep(4)
    }

    private fun generateCode() {
        generatedCode = Random.nextInt(100000, 999999).toString()
        tvDemoCode.text = generatedCode
    }

    private fun startTimer() {
        timer?.cancel()

        timer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / 1000
                val min = sec / 60
                val remain = sec % 60
                tvTimer.text = "%02d:%02d".format(min, remain)
            }

            override fun onFinish() {
                tvTimer.text = "00:00"
                showError("인증 코드가 만료되었습니다. 다시 요청해주세요")
            }
        }.start()
    }

    private fun showStep(newStep: Int) {
        step = newStep
        hideError()

        layoutEmail.visibility = View.GONE
        layoutVerify.visibility = View.GONE
        layoutReset.visibility = View.GONE
        layoutDone.visibility = View.GONE
        btnResend.visibility = View.GONE

        when (step) {
            1 -> {
                tvTitle.text = "이메일 확인"
                tvSubtitle.text = "가입 시 사용한 이메일을 입력해주세요"
                layoutEmail.visibility = View.VISIBLE
                btnNext.text = "인증 코드 받기"
            }

            2 -> {
                tvTitle.text = "인증 코드 확인"
                tvSubtitle.text = "화면에 표시된 데모 인증 코드를 입력해주세요"
                layoutVerify.visibility = View.VISIBLE
                btnResend.visibility = View.VISIBLE
                btnNext.text = "확인"
            }

            3 -> {
                tvTitle.text = "새 비밀번호 설정"
                tvSubtitle.text = "새로운 비밀번호를 설정해주세요"
                layoutReset.visibility = View.VISIBLE
                btnNext.text = "비밀번호 변경 완료"
            }

            4 -> {
                tvTitle.text = "변경 완료"
                tvSubtitle.text = "새 비밀번호로 성공적으로 변경되었습니다"
                layoutDone.visibility = View.VISIBLE
                btnNext.text = "로그인하러 가기"
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

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}