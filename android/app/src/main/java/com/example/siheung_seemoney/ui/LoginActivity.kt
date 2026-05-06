package com.example.siheung_seemoney.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.databinding.ActivityLoginBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val client = OkHttpClient()
    private val TAG = "LOGIN_API"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 레이아웃 인플레이터를 사용하여 ActivityLoginBinding 인스턴스를 생성
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 시 입력된 이메일과 비밀번호를 가져와 로그인 API 호출
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 서버에 로그인 요청을 보내는 함수
    private fun login(email: String, password: String) {
        val json = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            json
        )

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/api/auth/login") // 포트 확인 필요
            .post(requestBody)
            .build()

        Log.d(TAG, "요청 URL: ${request.url}")
        Log.d(TAG, "요청 Body: $json")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "서버 연결 실패", e)

                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "서버 연결 실패: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()

                Log.d(TAG, "응답 코드: ${response.code}")
                Log.d(TAG, "응답 Body: $result")

                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "로그인 성공: $result", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패: $result", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}