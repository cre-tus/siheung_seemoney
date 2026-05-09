package com.example.siheung_seemoney.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.siheung_seemoney.data.model.CategoryBudget
import com.example.siheung_seemoney.data.repository.FinanceRepository
import kotlinx.coroutines.launch

/**
 * 분석 화면(AnalysisActivity)의 데이터를 관리하는 ViewModel.
 * Repository로부터 분야별 예산 데이터를 가져와 View(Activity)에 제공합니다.
 */
class AnalysisViewModel : ViewModel() {

    private val repository = FinanceRepository()

    // 뷰에서 관찰할 수 있도록 노출하는 분야별 예산 리스트 (LiveData)
    private val _categoryBudgets = MutableLiveData<List<CategoryBudget>>()
    val categoryBudgets: LiveData<List<CategoryBudget>> get() = _categoryBudgets

    init {
        // ViewModel 생성 시 초기 데이터를 로드합니다. (예: 2026년)
        loadCategoryBudgetsForYear(2026)
    }

    /**
     * Repository를 통해 특정 연도의 분야별 예산 데이터를 로드합니다.
     */
    fun loadCategoryBudgetsForYear(year: Int) {
        viewModelScope.launch {
            try {
                val budgets = repository.getCategoryBudgets(year)
                _categoryBudgets.value = budgets
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
