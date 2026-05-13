package com.example.siheung_seemoney.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.siheung_seemoney.data.News
import com.example.siheung_seemoney.data.repository.FinanceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 홈 화면의 재정 타이머를 관리하는 ViewModel.
 *
 * [동작 방식]
 * 1. Repository에서 기준 데이터를 1회 가져옴 (지금은 MOCK, 나중에 API)
 * 2. 연간 변화량을 초당 변화량으로 환산
 * 3. 1초마다 예산은 감소, 부채는 증가시켜 LiveData로 전달
 *
 * [API 연동 후 변경점]
 * - Repository.getFinanceSummary() 가 실제 API 호출로 교체되면
 *   이 ViewModel은 수정 없이 그대로 동작함.
 */
class HomeViewModel : ViewModel() {

    private val repository = FinanceRepository()

    // 매 틱마다 업데이트되는 현재 예산 값
    private val _currentBudget = MutableLiveData<Long>()
    val currentBudget: LiveData<Long> = _currentBudget

    // 매 틱마다 업데이트되는 현재 부채 값
    private val _currentDebt = MutableLiveData<Long>()
    val currentDebt: LiveData<Long> = _currentDebt

    // 실시간 뉴스 리스트
    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> = _newsList

    // 1초 간격으로 값 업데이트 (타이머)
    private val TICK_INTERVAL_MS = 1_000L

    // 1분 간격으로 뉴스 업데이트
    private val NEWS_POLLING_INTERVAL_MS = 60_000L

    // 1년의 총 초수 (365일 * 24시간 * 3600초)
    private val SECONDS_IN_YEAR = 365.0 * 24 * 3600

    /**
     * 데모 배속 배율. 실제 속도(1.0)로 하면 백만원 단위에서 변화가 눈에 안 보임.
     * - 1.0   = 실제 속도 (백만원 단위에서 약 20초에 1 감소)
     * - 200.0 = 200배속 (약 0.1초에 1 감소, 데모용)
     * API 연동 후 실제 데이터 단위(원)로 바꾸면 1.0으로 돌려놓을 것.
     */
    private val SIMULATION_SPEED = 200.0

    private var timerJob: Job? = null
    private var newsJob: Job? = null

    /**
     * 재정 타이머 및 뉴스 폴링을 시작한다. 화면 진입 시 1회 호출.
     */
    fun startHomeData() {
        startFinanceTimer()
        startNewsPolling()
    }

    /**
     * 재정 타이머를 시작한다. 화면 진입 시 1회 호출.
     * - 내부적으로 Double을 누적하여 소수점 오차 없이 Long 표시값을 계산
     * - ViewModel이 소멸(화면 종료)되면 타이머 자동 중지
     */
    fun startFinanceTimer() {
        // 이전 타이머 취소 후 새로 시작
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            try {
                // API 연동으로 suspend 함수가 되었으므로 여기서 대기
                val data = repository.getFinanceSummary()

                // Double로 누적 추적 (소수점 이하 변화량도 정확히 쌓임)
                var budget = data.usedBudget.toDouble()
                var debt   = data.currentDebt.toDouble()

                // 초당 변화량 (SIMULATION_SPEED 배율 적용)
                val budgetDecreasePerTick = (data.annualBudgetDecrease.toDouble() / SECONDS_IN_YEAR) * SIMULATION_SPEED
                val debtIncreasePerTick   = (data.annualDebtIncrease.toDouble()   / SECONDS_IN_YEAR) * SIMULATION_SPEED

                // 초기값 즉시 설정
                _currentBudget.value = budget.toLong()
                _currentDebt.value   = debt.toLong()

                // 타이머 루프 시작
                while (true) {
                    delay(TICK_INTERVAL_MS)
                    // 예산은 0 아래로 내려가지 않음 (음수 방지)
                    budget = maxOf(0.0, budget - budgetDecreasePerTick)
                    debt   += debtIncreasePerTick
                    _currentBudget.value = budget.toLong()
                    _currentDebt.value   = debt.toLong()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 1분 주기로 실시간 뉴스를 가져온다.
     */
    private fun startNewsPolling() {
        newsJob?.cancel()
        newsJob = viewModelScope.launch {
            while (true) {
                try {
                    val news = repository.getNews()
                    // 이전 데이터와 다를 경우에만 UI 업데이트
                    if (news != _newsList.value) {
                        _newsList.postValue(news)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(NEWS_POLLING_INTERVAL_MS)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // 화면 종료 시 모든 코루틴 중지
        timerJob?.cancel()
        newsJob?.cancel()
    }
}
