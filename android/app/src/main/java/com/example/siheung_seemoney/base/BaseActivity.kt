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
import com.example.siheung_seemoney.ui.MainActivity
import com.example.siheung_seemoney.ui.MyPageActivity
import com.example.siheung_seemoney.ui.ParticipateActivity

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    abstract fun inflateBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflateBinding()
        setContentView(binding.root)

        setupBottomNav()
    }

    private fun setupBottomNav() {
        binding.root.findViewById<TextView>(R.id.navHome)?.setOnClickListener {
            moveTo(MainActivity::class.java)
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
    }

    private fun moveTo(targetActivity: Class<*>) {
        if (this::class.java == targetActivity) return

        val intent = Intent(this, targetActivity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}