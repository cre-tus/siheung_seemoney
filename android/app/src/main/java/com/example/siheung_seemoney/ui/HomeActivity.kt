package com.example.siheung_seemoney.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.data.repository.FinanceRepository
import com.example.siheung_seemoney.databinding.ActivityHomeBinding
import com.example.siheung_seemoney.view_model.HomeViewModel

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    // 매 틱 애니메이션을 위한 직전 값 추적
    private var prevBudget = 0L
    private var prevDebt   = 0L

    override fun inflateBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 시스템 바 인셋 처리
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeTimer()
        bindCategories()
        viewModel.startFinanceTimer()
    }

    /**
     * 매 틱(1초)마다 예산/부채 값을 받아 AnimatedNumberTextView로 부드럽게 갱신.
     * 직전 값 -> 현재 값 950ms 애니메이션으로 다음 틱 전에 완료됨.
     */
    private fun observeTimer() {
        viewModel.currentBudget.observe(this) { budget ->
            binding.tvBudgetAmount.animateToNumber(
                startValue = if (prevBudget == 0L) budget else prevBudget,
                endValue   = budget,
                duration   = 950L
            )
            prevBudget = budget
        }

        viewModel.currentDebt.observe(this) { debt ->
            binding.tvDebtAmount.animateToNumber(
                startValue = if (prevDebt == 0L) debt else prevDebt,
                endValue   = debt,
                duration   = 950L
            )
            prevDebt = debt
        }
    }

    /**
     * 예산 구조 카테고리 퍼센트 및 ProgressBar를 MOCK 데이터로 바인딩.
     * API 연동 시 ViewModel의 LiveData로 교체 예정.
     */
    private fun bindCategories() {
        val categories = FinanceRepository.MOCK.categories

        val categoryViews = listOf(
            Triple(binding.tvCategoryWelfare,     binding.progressWelfare,     categories.getOrNull(0)),
            Triple(binding.tvCategoryTransport,   binding.progressTransport,   categories.getOrNull(1)),
            Triple(binding.tvCategoryEnvironment, binding.progressEnvironment, categories.getOrNull(2)),
            Triple(binding.tvCategoryEtc,         binding.progressEtc,         categories.getOrNull(3))
        )

        for ((textView, progressBar, category) in categoryViews) {
            if (category != null) {
                textView.text      = "${category.name}${" ".repeat(40)}${category.percentage}%"
                progressBar.progress = category.percentage
            }
        }
    }
}