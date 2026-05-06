package com.example.siheung_seemoney.ui

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityHomeBinding

class MainActivity : BaseActivity<ActivityHomeBinding>() {

    // 레이아웃 인플레이터를 사용하여 ActivityHomeBinding 인스턴스를 생성
    override fun inflateBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 시스템 바(상태바, 내비게이션 바)의 인셋을 감지하여 뷰의 패딩을 조정
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

        // 홈 화면 진입 시 타이머 및 스톱워치 애니메이션 실행 (더미 데이터 적용)
        // 예산 감소: 1조 6천억 원 -> 1조 5,800억 원
        binding.tvBudgetAmount.animateToNumber(
            startValue = 1_600_000_000_000L,
            endValue = 1_580_000_000_000L,
            duration = 2500L, // 2.5초 지속
            suffix = "원"
        )

        // 부채 증가: 1,200억 원 -> 1,250억 원
        binding.tvDebtAmount.animateToNumber(
            startValue = 120_000_000_000L,
            endValue = 125_000_000_000L,
            duration = 2500L, // 2.5초 지속
            suffix = "원"
        )
    }
}