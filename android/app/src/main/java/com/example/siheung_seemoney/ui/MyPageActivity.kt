package com.example.siheung_seemoney.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R

class MyPageActivity : AppCompatActivity() {

    data class ActivityItem(
        val id: Int,
        val type: String,
        val title: String,
        val date: String,
        val points: Int
    )

    data class Reward(
        val level: String,
        val minPoints: Int,
        val maxPoints: Int,
        val benefits: String
    )

    private val rewards = listOf(
        Reward("브론즈", 0, 499, "뉴스 열람"),
        Reward("실버", 500, 999, "투표 참여 + 뱃지"),
        Reward("골드", 1000, 1999, "제안 작성 + 특별 뱃지"),
        Reward("플래티넘", 2000, 9999, "우선 답변 + 프리미엄 뱃지")
    )

    private val activities = listOf(
        ActivityItem(1, "vote", "복지 예산 투표 참여", "2026.05.01", 100),
        ActivityItem(2, "post", "청년 창업 지원 예산 제안 작성", "2026.05.02", 100),
        ActivityItem(3, "like", "시청 앞 공원 재정비 공감", "2026.05.03", 50),
        ActivityItem(4, "comment", "문화센터 야간 운영 댓글 작성", "2026.05.04", 30)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val tvGrade = findViewById<TextView>(R.id.tvGrade)
        val tvPoint = findViewById<TextView>(R.id.tvPoint)
        val tvVote = findViewById<TextView>(R.id.tvVote)
        val tvPost = findViewById<TextView>(R.id.tvPost)

        val tvNextPoint = findViewById<TextView>(R.id.tvNextPoint)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvGradePath = findViewById<TextView>(R.id.tvGradePath)

        val rewardContainer = findViewById<LinearLayout>(R.id.rewardContainer)
        val historyContainer = findViewById<LinearLayout>(R.id.historyContainer)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // 임시 세션 데이터
        val email = "guest@example.com"
        val userName = email.substringBefore("@")
        val point = 320
        val currentGrade = "브론즈"

        val postsCount = activities.count { it.type == "post" }
        val votesCount = activities.count { it.type == "vote" }

        tvUserName.text = userName
        tvGrade.text = "$currentGrade 등급"
        tvPoint.text = point.toString()
        tvVote.text = votesCount.toString()
        tvPost.text = postsCount.toString()

        val currentLevel = rewards.find {
            point >= it.minPoints && point <= it.maxPoints
        } ?: rewards.first()

        val currentIndex = rewards.indexOf(currentLevel)
        val nextLevel = rewards.getOrNull(currentIndex + 1)

        val remainPoint = currentLevel.maxPoints - point
        tvNextPoint.text = "${remainPoint}P 남음"

        val progress =
            (((point - currentLevel.minPoints).toFloat() /
                    (currentLevel.maxPoints - currentLevel.minPoints).toFloat()) * 100).toInt()

        progressBar.progress = progress

        tvGradePath.text =
            "${currentLevel.level} → ${nextLevel?.level ?: "최고 등급"}"

        renderRewards(rewardContainer, point, currentLevel)
        renderActivities(historyContainer)

        btnLogout.setOnClickListener {
            Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun renderRewards(
        container: LinearLayout,
        point: Int,
        currentLevel: Reward
    ) {
        container.removeAllViews()

        for (reward in rewards) {
            val isCurrent = reward == currentLevel
            val isPassed = point > reward.maxPoints

            val box = LinearLayout(this)
            box.orientation = LinearLayout.VERTICAL
            box.setPadding(18, 14, 18, 14)

            box.setBackgroundColor(
                when {
                    isCurrent -> 0xFFEFF6FF.toInt()
                    isPassed -> 0xFFF0FDF4.toInt()
                    else -> 0xFFFFFFFF.toInt()
                }
            )

            val titleRow = LinearLayout(this)
            titleRow.orientation = LinearLayout.HORIZONTAL

            val title = TextView(this)
            title.text = reward.level
            title.textSize = 15f
            title.setTextColor(0xFF111827.toInt())
            title.setTypeface(null, Typeface.BOLD)
            title.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )

            val badge = TextView(this)
            badge.text = when {
                isCurrent -> "현재"
                isPassed -> "달성"
                else -> ""
            }
            badge.textSize = 11f
            badge.setPadding(12, 4, 12, 4)
            badge.setTextColor(0xFFFFFFFF.toInt())
            badge.setBackgroundColor(
                when {
                    isCurrent -> 0xFF2563EB.toInt()
                    isPassed -> 0xFF22C55E.toInt()
                    else -> 0x00000000
                }
            )

            titleRow.addView(title)
            if (badge.text.isNotEmpty()) titleRow.addView(badge)

            val benefit = TextView(this)
            benefit.text = reward.benefits
            benefit.textSize = 13f
            benefit.setTextColor(0xFF6B7280.toInt())
            benefit.setPadding(0, 8, 0, 0)

            val range = TextView(this)
            range.text = "${reward.minPoints}P~"
            range.textSize = 13f
            range.setTextColor(0xFF6B7280.toInt())
            range.setPadding(0, 4, 0, 0)

            box.addView(titleRow)
            box.addView(benefit)
            box.addView(range)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 10)

            container.addView(box, params)
        }
    }

    private fun renderActivities(container: LinearLayout) {
        container.removeAllViews()

        val history = activities.takeLast(10).reversed()

        if (history.isEmpty()) {
            val empty = TextView(this)
            empty.text = "참여 내역이 없습니다"
            empty.textSize = 14f
            empty.setTextColor(0xFF6B7280.toInt())
            empty.setPadding(0, 24, 0, 24)
            empty.gravity = android.view.Gravity.CENTER
            container.addView(empty)
            return
        }

        for (item in history) {
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.setPadding(0, 14, 0, 14)

            val icon = TextView(this)
            icon.text = when (item.type) {
                "vote" -> "✓"
                "post" -> "💬"
                "like" -> "♥"
                else -> "💬"
            }
            icon.textSize = 20f
            icon.gravity = android.view.Gravity.CENTER
            icon.layoutParams = LinearLayout.LayoutParams(48, 48)

            val textBox = LinearLayout(this)
            textBox.orientation = LinearLayout.VERTICAL
            textBox.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            textBox.setPadding(12, 0, 12, 0)

            val title = TextView(this)
            title.text = item.title
            title.textSize = 14f
            title.setTextColor(0xFF111827.toInt())
            title.setTypeface(null, Typeface.BOLD)

            val date = TextView(this)
            date.text = item.date
            date.textSize = 12f
            date.setTextColor(0xFF6B7280.toInt())

            textBox.addView(title)
            textBox.addView(date)

            val point = TextView(this)
            point.text = "+${item.points}P"
            point.textSize = 14f
            point.setTextColor(0xFF2563EB.toInt())
            point.setTypeface(null, Typeface.BOLD)

            row.addView(icon)
            row.addView(textBox)
            row.addView(point)

            container.addView(row)
        }
    }
}