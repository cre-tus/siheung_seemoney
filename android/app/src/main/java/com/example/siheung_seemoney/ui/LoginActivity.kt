package com.example.siheung_seemoney.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val TAG = "LOGIN_API"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            login(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

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
            .url("http://10.0.2.2:8081/api/auth/login")
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