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
    private var prevDebt = 0L

    override fun inflateBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        observeTimer()
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
                endValue = budget,
                duration = 950L
            )
            prevBudget = budget
        }

        viewModel.currentDebt.observe(this) { debt ->
            binding.tvDebtAmount.animateToNumber(
                startValue = if (prevDebt == 0L) debt else prevDebt,
                endValue = debt,
                duration = 950L
            )
            prevDebt = debt
        }
    }
}
