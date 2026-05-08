package com.example.siheung_seemoney.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val etAddress = findViewById<EditText>(R.id.etAddress)

        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val cbPrivacy = findViewById<CheckBox>(R.id.cbPrivacy)
        val cbMarketing = findViewById<CheckBox>(R.id.cbMarketing)

        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnSignup.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val address = etAddress.text.toString().trim()

            tvError.visibility = View.GONE

            if (email.isEmpty()) {
                showError(tvError, "이메일을 입력해주세요")
                return@setOnClickListener
            }

            if (password.length < 8) {
                showError(tvError, "비밀번호는 8자 이상이어야 합니다")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showError(tvError, "비밀번호가 일치하지 않습니다")
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                showError(tvError, "주소를 입력해주세요")
                return@setOnClickListener
            }

            if (!cbTerms.isChecked || !cbPrivacy.isChecked) {
                showError(tvError, "필수 약관에 동의해주세요")
                return@setOnClickListener
            }

            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showError(tv: TextView, msg: String) {
        tv.text = msg
        tv.visibility = View.VISIBLE
    }
}