package com.example.siheung_seemoney

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siheung_seemoney.databinding.ActivityHomeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 하단 네비게이션
        binding.navHome.setOnClickListener {
            // 현재 홈
        }

        binding.navAnalysis.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        binding.navParticipate.setOnClickListener {
            startActivity(Intent(this, ParticipateActivity::class.java))
        }

        binding.navBoard.setOnClickListener {
            startActivity(Intent(this, BoardActivity::class.java))
        }

        binding.navMyPage.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }

        // 인셋 처리
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }
}