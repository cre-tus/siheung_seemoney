package com.example.siheung_seemoney.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siheung_seemoney.adapter.NewsAdapter
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityHomeBinding
import com.example.siheung_seemoney.ui.news.NewsDetailActivity
import com.example.siheung_seemoney.view_model.HomeViewModel

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    // 매 틱 애니메이션을 위한 직전 값 추적
    private var prevBudget = 0L
    private var prevDebt = 0L

    override fun inflateBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNewsRecyclerView()
        observeViewModel()
        
        // 홈 데이터(타이머 + 뉴스) 시작
        viewModel.startHomeData()
    }

    private fun setupNewsRecyclerView() {
        newsAdapter = NewsAdapter { news ->
            // 뉴스 클릭 시 상세 화면으로 이동
            val intent = Intent(this, NewsDetailActivity::class.java).apply {
                putExtra("title", news.title)
                putExtra("summary", news.summary)
                putExtra("pubDate", news.pubDate)
                putExtra("link", news.link)
            }
            startActivity(intent)
        }

        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        // 타이머 관찰
        viewModel.currentBudget.observe(this) { budget ->
            binding.tvBudgetAmount.animateToNumber(
                startValue = if (prevBudget == 0L) budget else prevBudget,
                endValue = budget,
                duration = 950L
            )
            // 우측 상단: 조 단위 (예산은 수조 원대)
            val trillionBudget = budget / 1_000_000_000_000.0
            binding.tvBudgetUnit.text = String.format("%.1f조원", trillionBudget)
            
            prevBudget = budget
        }

        viewModel.currentDebt.observe(this) { debt ->
            binding.tvDebtAmount.animateToNumber(
                startValue = if (prevDebt == 0L) debt else prevDebt,
                endValue = debt,
                duration = 950L
            )
            // 우측 상단: 억 단위 (부채는 약 1,700~1,900억 원대)
            val hundredMillionDebt = debt / 100_000_000.0
            binding.tvDebtUnit.text = String.format("%.0f억원", hundredMillionDebt)
            
            prevDebt = debt
        }

        // 뉴스 리스트 관찰
        viewModel.newsList.observe(this) { news ->
            newsAdapter.submitList(news)
        }
    }
}
