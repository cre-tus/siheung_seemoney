package com.example.siheung_seemoney.base

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.siheung_seemoney.R
import com.example.siheung_seemoney.ui.AnalysisActivity
import com.example.siheung_seemoney.ui.BoardActivity
import com.example.siheung_seemoney.ui.LoginActivity
import com.example.siheung_seemoney.ui.HomeActivity
import com.example.siheung_seemoney.ui.MyPageActivity
import com.example.siheung_seemoney.ui.ParticipateActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    abstract fun inflateBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflateBinding()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        binding.root.findViewById<TextView>(R.id.navHome)?.setOnClickListener {
            moveTo(HomeActivity::class.java)
        }

        binding.root.findViewById<TextView>(R.id.navAnalysis)?.setOnClickListener {
            moveTo(AnalysisActivity::class.java)
        }

        binding.root.findViewById<TextView>(R.id.navParticipate)?.setOnClickListener {
            moveTo(ParticipateActivity::class.java)
        }

        binding.root.findViewById<TextView>(R.id.navBoard)?.setOnClickListener {
            moveTo(BoardActivity::class.java)
        }

        binding.root.findViewById<TextView>(R.id.navMyPage)?.setOnClickListener {
            moveTo(LoginActivity::class.java)
        }

        updateBottomNavUI()
    }

    private fun updateBottomNavUI() {
        val navHome = binding.root.findViewById<TextView>(R.id.navHome) ?: return
        val navAnalysis = binding.root.findViewById<TextView>(R.id.navAnalysis) ?: return
        val navParticipate = binding.root.findViewById<TextView>(R.id.navParticipate) ?: return
        val navBoard = binding.root.findViewById<TextView>(R.id.navBoard) ?: return
        val navMyPage = binding.root.findViewById<TextView>(R.id.navMyPage) ?: return

        val colorActive = android.graphics.Color.parseColor("#2563EB")
        val colorInactive = android.graphics.Color.parseColor("#6B7280")

        navHome.setTextColor(colorInactive)
        navAnalysis.setTextColor(colorInactive)
        navParticipate.setTextColor(colorInactive)
        navBoard.setTextColor(colorInactive)
        navMyPage.setTextColor(colorInactive)

        when (this::class.java) {
            HomeActivity::class.java -> navHome.setTextColor(colorActive)
            AnalysisActivity::class.java -> navAnalysis.setTextColor(colorActive)
            ParticipateActivity::class.java -> navParticipate.setTextColor(colorActive)
            BoardActivity::class.java -> navBoard.setTextColor(colorActive)
            LoginActivity::class.java, MyPageActivity::class.java -> navMyPage.setTextColor(colorActive)
        }
    }

    private fun moveTo(targetActivity: Class<*>) {
        if (this::class.java == targetActivity) return

        val intent = Intent(this, targetActivity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}