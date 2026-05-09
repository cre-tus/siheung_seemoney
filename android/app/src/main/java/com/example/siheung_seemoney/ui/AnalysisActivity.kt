package com.example.siheung_seemoney.ui

import com.example.siheung_seemoney.R
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityAnalysisBinding
import com.example.siheung_seemoney.ui.adapter.CategoryBudgetAdapter
import com.example.siheung_seemoney.view_model.AnalysisViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

/**
 * 분석 화면 Activity.
 * 분야별 예산 상세 리스트(RecyclerView)와 연도별/분야별 차트(MPAndroidChart)를 표시합니다.
 * MVVM 패턴을 적용하여 AnalysisViewModel을 관찰(Observe)합니다.
 */
class AnalysisActivity : BaseActivity<ActivityAnalysisBinding>() {

    // ViewModel 초기화 (by viewModels() 위임을 통해 생명주기 관리)
    private val viewModel: AnalysisViewModel by viewModels()
    // 분야별 예산 리스트를 연결할 어댑터
    private lateinit var adapter: CategoryBudgetAdapter

    // ViewBinding 인플레이트
    override fun inflateBinding(): ActivityAnalysisBinding {
        return ActivityAnalysisBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRecyclerView()
        setupCharts()
        setupYearButtons()
        observeViewModel()
    }

    /**
     * 상단 연도 버튼들에 클릭 리스너를 설정합니다.
     */
    private fun setupYearButtons() {
        val yearButtons = mapOf(
            2022 to binding.year2022,
            2023 to binding.year2023,
            2024 to binding.year2024,
            2025 to binding.year2025,
            2026 to binding.year2026
        )

        yearButtons.forEach { (year, textView) ->
            textView.setOnClickListener {
                // 모든 버튼 스타일 초기화 (회색 배경)
                yearButtons.values.forEach { 
                    it.setBackgroundResource(R.drawable.bg_button_gray)
                    it.setTextColor(Color.parseColor("#111827"))
                }
                // 클릭된 버튼 강조 (파란색 배경)
                textView.setBackgroundResource(R.drawable.bg_button_blue)
                textView.setTextColor(Color.WHITE)
                
                // 데이터 로드
                viewModel.loadCategoryBudgetsForYear(year)
            }
        }
    }

    /**
     * RecyclerView 어댑터 초기화 및 바인딩.
     */
    private fun setupRecyclerView() {
        adapter = CategoryBudgetAdapter()
        binding.rvCategoryBudgets.adapter = adapter
    }

    /**
     * ViewModel의 LiveData를 관찰하여 UI(어댑터)를 업데이트합니다.
     */
    private fun observeViewModel() {
        viewModel.categoryBudgets.observe(this, adapter::submitList)
    }

    /**
     * MPAndroidChart 라이브러리를 사용한 LineChart(연도별 변화)와 BarChart(분야별 비교) 설정.
     * 현재는 UI 구성을 위한 Mock 데이터를 하드코딩하여 렌더링하고 있습니다.
     */
    private fun setupCharts() {
        // 1. LineChart 설정 (연도별 예산/부채 변화)
        val lineChart = binding.lineChart
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        val xAxisLine = lineChart.xAxis
        xAxisLine.position = XAxis.XAxisPosition.BOTTOM
        xAxisLine.valueFormatter = IndexAxisValueFormatter(listOf("2022", "2023", "2024", "2025", "2026"))
        xAxisLine.granularity = 1f
        xAxisLine.setDrawGridLines(false)

        lineChart.axisRight.isEnabled = false

        // Mock 데이터
        val budgetEntries = ArrayList<Entry>()
        budgetEntries.add(Entry(0f, 12000f))
        budgetEntries.add(Entry(1f, 13000f))
        budgetEntries.add(Entry(2f, 15000f))
        budgetEntries.add(Entry(3f, 15500f))
        budgetEntries.add(Entry(4f, 16000f))

        val debtEntries = ArrayList<Entry>()
        debtEntries.add(Entry(0f, 800f))
        debtEntries.add(Entry(1f, 900f))
        debtEntries.add(Entry(2f, 1100f))
        debtEntries.add(Entry(3f, 1150f))
        debtEntries.add(Entry(4f, 1200f))

        val budgetDataSet = LineDataSet(budgetEntries, "예산").apply {
            color = Color.parseColor("#3B82F6")
            setCircleColor(Color.parseColor("#3B82F6"))
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false)
        }

        val debtDataSet = LineDataSet(debtEntries, "부채").apply {
            color = Color.parseColor("#EF4444")
            setCircleColor(Color.parseColor("#EF4444"))
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false)
        }

        val lineData = LineData(budgetDataSet, debtDataSet)
        lineChart.data = lineData
        lineChart.invalidate()

        // 차트 클릭 시 연도별 데이터 로드
        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val index = e?.x?.toInt() ?: return
                val years = listOf(2022, 2023, 2024, 2025, 2026)
                if (index in years.indices) {
                    val selectedYear = years[index]
                    viewModel.loadCategoryBudgetsForYear(selectedYear)
                }
            }

            override fun onNothingSelected() {
                // 선택 해제 시 추가 동작 필요없음
            }
        })
    }
}