package com.example.siheung_seemoney.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R

class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val tvGrade = findViewById<TextView>(R.id.tvGrade)
        val tvPoint = findViewById<TextView>(R.id.tvPoint)
        val tvVote = findViewById<TextView>(R.id.tvVote)
        val tvPost = findViewById<TextView>(R.id.tvPost)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvNextGrade = findViewById<TextView>(R.id.tvNextGrade)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val historyContainer = findViewById<LinearLayout>(R.id.historyContainer)

        // 임시 데이터
        val userName = "진영"
        val grade = "브론즈"
        val point = 320
        val voteCount = 5
        val postCount = 2

        tvUserName.text = userName
        tvGrade.text = "$grade 등급"
        tvPoint.text = point.toString()
        tvVote.text = voteCount.toString()
        tvPost.text = postCount.toString()

        progressBar.progress = 64
        tvNextGrade.text = "실버까지 180P 남음"

        val activities = listOf(
            "복지 정책 투표 참여 +100P",
            "교통 정책 제안 작성 +100P",
            "환경 정책 공감하기 +50P"
        )

        for (item in activities) {

            val textView = TextView(this)
            textView.text = item
            textView.textSize = 14f
            textView.setPadding(0, 12, 0, 12)

            historyContainer.addView(textView)
        }

        btnLogout.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}