package com.example.siheung_seemoney.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.siheung_seemoney.R
import com.example.siheung_seemoney.ui.AnalysisActivity
import com.example.siheung_seemoney.ui.BoardActivity
import com.example.siheung_seemoney.ui.HomeActivity
import com.example.siheung_seemoney.ui.LoginActivity
import com.example.siheung_seemoney.ui.MyPageActivity
import com.example.siheung_seemoney.ui.ParticipateActivity

/**
 * 공통 BaseActivity
 * - ViewBinding 공통 처리
 * - 하단 네비게이션 공통 처리
 * - 현재 탭 색상 처리
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    /**
     * 각 Activity에서 ViewBinding 생성
     */
    abstract fun inflateBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // =========================
        // ViewBinding 연결
        // =========================
        binding =
            inflateBinding()

        setContentView(binding.root)

        // =========================
        // 상태바 Insets 처리
        // =========================
        val contentContainer =
            binding.root.findViewById<View>(
                R.id.contentContainer
            )

        contentContainer?.let {

            ViewCompat.setOnApplyWindowInsetsListener(it) { view, insets ->

                val systemBars =
                    insets.getInsets(
                        WindowInsetsCompat.Type.systemBars()
                    )

                view.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    0
                )

                insets
            }
        }

        // =========================
        // 하단 네비게이션 설정
        // =========================
        setupBottomNav()
    }

    /**
     * 하단 네비게이션 설정
     */
    private fun setupBottomNav() {

        val navHome =
            binding.root.findViewById<TextView>(
                R.id.navHome
            )

        val navAnalysis =
            binding.root.findViewById<TextView>(
                R.id.navAnalysis
            )

        val navParticipate =
            binding.root.findViewById<TextView>(
                R.id.navParticipate
            )

        val navBoard =
            binding.root.findViewById<TextView>(
                R.id.navBoard
            )

        val navMyPage =
            binding.root.findViewById<TextView>(
                R.id.navMyPage
            )

        // =========================
        // include_bottom_nav 없는 화면
        // =========================
        if (
            navHome == null ||
            navAnalysis == null ||
            navParticipate == null ||
            navBoard == null ||
            navMyPage == null
        ) {
            return
        }

        // =========================
        // 홈
        // =========================
        navHome.setOnClickListener {

            Log.d("NAV", "Home")
            moveTo(

                HomeActivity::class.java
            )
        }

        // =========================
        // 분석
        // =========================
        navAnalysis.setOnClickListener {

            moveTo(
                AnalysisActivity::class.java
            )
        }

        // =========================
        // 참여
        // =========================
        navParticipate.setOnClickListener {

            moveTo(
                ParticipateActivity::class.java
            )
        }

        // =========================
        // 게시판
        // =========================
        navBoard.setOnClickListener {

            moveTo(
                BoardActivity::class.java
            )
        }

        // =========================
        // 마이페이지
        // =========================
        navMyPage.setOnClickListener {

            println("MY CLICK")

            val token =
                getSharedPreferences(
                    "auth_prefs",
                    MODE_PRIVATE
                ).getString(
                    "jwt_token",
                    null
                )
            println("TOKEN = $token")

            if (token != null) {

                moveTo(MyPageActivity::class.java)

            } else {

                moveTo(LoginActivity::class.java)
            }
        }

        // =========================
        // 현재 탭 UI 반영
        // =========================
        updateBottomNavUI()
    }

    /**
     * 현재 선택된 탭 색상 처리
     */
    private fun updateBottomNavUI() {

        val navHome =
            binding.root.findViewById<TextView>(
                R.id.navHome
            ) ?: return

        val navAnalysis =
            binding.root.findViewById<TextView>(
                R.id.navAnalysis
            ) ?: return

        val navParticipate =
            binding.root.findViewById<TextView>(
                R.id.navParticipate
            ) ?: return

        val navBoard =
            binding.root.findViewById<TextView>(
                R.id.navBoard
            ) ?: return

        val navMyPage =
            binding.root.findViewById<TextView>(
                R.id.navMyPage
            ) ?: return

        val colorActive =
            Color.parseColor("#2563EB")

        val colorInactive =
            Color.parseColor("#6B7280")

        // =========================
        // 초기화
        // =========================
        navHome.setTextColor(colorInactive)

        navAnalysis.setTextColor(colorInactive)

        navParticipate.setTextColor(colorInactive)

        navBoard.setTextColor(colorInactive)

        navMyPage.setTextColor(colorInactive)

        // =========================
        // 현재 화면 활성화
        // =========================
        when (this::class.java) {

            HomeActivity::class.java -> {

                navHome.setTextColor(
                    colorActive
                )
            }

            AnalysisActivity::class.java -> {

                navAnalysis.setTextColor(
                    colorActive
                )
            }

            ParticipateActivity::class.java -> {

                navParticipate.setTextColor(
                    colorActive
                )
            }

            BoardActivity::class.java -> {

                navBoard.setTextColor(
                    colorActive
                )
            }

            LoginActivity::class.java,
            MyPageActivity::class.java -> {

                navMyPage.setTextColor(
                    colorActive
                )
            }
        }
    }

    /**
     * Activity 이동
     */
    private fun moveTo(
        targetActivity: Class<*>
    ) {

        // 현재 화면이면 이동 안 함
        if (
            this::class.java ==
            targetActivity
        ) {
            return
        }

        val intent =
            Intent(
                this,
                targetActivity
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

        startActivity(intent)

        overridePendingTransition(
            0,
            0
        )

        finish()
    }
}