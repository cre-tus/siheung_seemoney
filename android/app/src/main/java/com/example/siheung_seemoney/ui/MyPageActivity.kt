package com.example.siheung_seemoney.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R

class MyPageActivity : AppCompatActivity() {

    data class ActivityItem(
        val type: String,
        val title: String,
        val date: String,
        val points: Int
    )

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
        val tvGradePath = findViewById<TextView>(R.id.tvGradePath)
        val rewardContainer = findViewById<LinearLayout>(R.id.rewardContainer)
        val historyContainer = findViewById<LinearLayout>(R.id.historyContainer)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val userName = "guest"
        val grade = "브론즈"
        val point = 320

        val activities = listOf(
            ActivityItem("vote", "복지 예산 투표 참여", "2026.05.01", 100),
            ActivityItem("post", "청년 창업 지원 예산 제안 작성", "2026.05.02", 100),
            ActivityItem("like", "시청 앞 공원 재정비 공감", "2026.05.03", 50),
            ActivityItem("comment", "문화센터 야간 운영 댓글 작성", "2026.05.04", 30)
        )

        val voteCount = activities.count { it.type == "vote" }
        val postCount = activities.count { it.type == "post" }

        tvUserName.text = userName
        tvGrade.text = "$grade 등급"
        tvPoint.text = point.toString()
        tvVote.text = voteCount.toString()
        tvPost.text = postCount.toString()

        val progress = ((point.toFloat() / 499f) * 100).toInt()
        progressBar.progress = progress
        tvNextGrade.text = "실버까지 ${499 - point}P 남음"
        tvGradePath.text = "브론즈 → 실버"

        addReward(rewardContainer, "브론즈", "0P~", "뉴스 열람", true, false)
        addReward(rewardContainer, "실버", "500P~", "투표 참여 + 뱃지", false, false)
        addReward(rewardContainer, "골드", "1000P~", "제안 작성 + 특별 뱃지", false, false)
        addReward(rewardContainer, "플래티넘", "2000P~", "우선 답변 + 프리미엄 뱃지", false, false)

        historyContainer.removeAllViews()

        if (activities.isEmpty()) {
            val empty = TextView(this)
            empty.text = "참여 내역이 없습니다"
            empty.textSize = 14f
            empty.setTextColor(0xFF6B7280.toInt())
            empty.setPadding(0, 20, 0, 20)
            historyContainer.addView(empty)
        } else {
            activities.reversed().forEach {
                addHistory(historyContainer, it)
            }
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun addReward(
        container: LinearLayout,
        level: String,
        range: String,
        benefit: String,
        isCurrent: Boolean,
        isPassed: Boolean
    ) {
        val box = LinearLayout(this)
        box.orientation = LinearLayout.VERTICAL
        box.setPadding(20, 16, 20, 16)

        val bgColor = when {
            isCurrent -> 0xFFEFF6FF.toInt()
            isPassed -> 0xFFF0FDF4.toInt()
            else -> 0xFFFFFFFF.toInt()
        }
        box.setBackgroundColor(bgColor)

        val title = TextView(this)
        title.text = if (isCurrent) "$level  현재" else level
        title.textSize = 16f
        title.setTypeface(null, Typeface.BOLD)
        title.setTextColor(0xFF111827.toInt())

        val sub = TextView(this)
        sub.text = "$benefit  ·  $range"
        sub.textSize = 13f
        sub.setTextColor(0xFF6B7280.toInt())

        box.addView(title)
        box.addView(sub)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 10)
        container.addView(box, params)
    }

    private fun addHistory(container: LinearLayout, item: ActivityItem) {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.setPadding(0, 14, 0, 14)

        val left = LinearLayout(this)
        left.orientation = LinearLayout.VERTICAL
        left.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)

        val title = TextView(this)
        title.text = item.title
        title.textSize = 15f
        title.setTextColor(0xFF111827.toInt())
        title.setTypeface(null, Typeface.BOLD)

        val date = TextView(this)
        date.text = item.date
        date.textSize = 12f
        date.setTextColor(0xFF6B7280.toInt())

        val point = TextView(this)
        point.text = "+${item.points}P"
        point.textSize = 15f
        point.setTypeface(null, Typeface.BOLD)
        point.setTextColor(0xFF2563EB.toInt())

        left.addView(title)
        left.addView(date)
        row.addView(left)
        row.addView(point)

        container.addView(row)
    }
}