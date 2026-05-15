package com.example.siheung_seemoney.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.siheung_seemoney.data.model.Poll
import com.example.siheung_seemoney.data.model.PollVoteState
import com.example.siheung_seemoney.data.repository.PollRepository

/**
 * 참여 화면(ParticipateActivity)의 투표 상태를 관리하는 ViewModel.
 *
 * [현재] PollRepository의 목업 데이터 + SharedPreferences 기반.
 * [추후 API 연동 시] repository.getPolls()가 suspend 함수가 되면
 * viewModelScope.launch { } 로 감싸서 비동기 처리.
 */
class ParticipationViewModel(context: Context) : ViewModel() {

    private val repository = PollRepository(context)

    // 투표 항목 리스트
    private val _polls = MutableLiveData<List<Poll>>()
    val polls: LiveData<List<Poll>> = _polls

    // pollId → 해당 투표의 현재 상태 (선택 옵션 + 집계)
    private val _voteStates = MutableLiveData<Map<Int, PollVoteState>>()
    val voteStates: LiveData<Map<Int, PollVoteState>> = _voteStates

    // 투표 완료 이벤트 (SnackBar 표시용) - pollId 전달
    private val _voteCompletedEvent = MutableLiveData<Int?>()
    val voteCompletedEvent: LiveData<Int?> = _voteCompletedEvent

    // 중복 투표 시도 이벤트 (Toast 표시용)
    private val _alreadyVotedEvent = MutableLiveData<Boolean>()
    val alreadyVotedEvent: LiveData<Boolean> = _alreadyVotedEvent

    init {
        loadPolls()
    }

    /**
     * 투표 항목을 불러오고 각 항목의 투표 상태를 초기화합니다.
     *
     * [추후 API 연동 시] viewModelScope.launch { _polls.value = repository.getPolls() } 형태로 교체.
     */
    private fun loadPolls() {
        val pollList = repository.getPolls()
        _polls.value = pollList

        val states = pollList.associate { poll ->
            poll.id to repository.getVoteState(poll)
        }
        _voteStates.value = states
    }

    /**
     * 특정 투표 항목에 투표를 처리합니다.
     *
     * @param poll 투표 대상 항목
     * @param optionIndex 선택한 옵션 인덱스
     *
     * [추후 API 연동 시]
     * viewModelScope.launch {
     *   val token = SharedPreferences에서 JWT 토큰 읽기
     *   val result = repository.vote(poll, optionIndex, token) // suspend 함수
     *   if (result.isSuccess) { ... }
     * }
     * 백엔드에서 vote_count +1 처리 필요 (PollRepository 주석 참고)
     */
    fun vote(poll: Poll, optionIndex: Int) {
        val success = repository.vote(poll, optionIndex)

        if (success) {
            // 투표 성공 → 상태 갱신 후 이벤트 발행
            val currentStates = _voteStates.value?.toMutableMap() ?: mutableMapOf()
            currentStates[poll.id] = repository.getVoteState(poll)
            _voteStates.value = currentStates
            _voteCompletedEvent.value = poll.id
        } else {
            // 이미 투표한 경우
            _alreadyVotedEvent.value = true
        }
    }

    /** SnackBar 표시 후 이벤트 소비 */
    fun onVoteCompletedEventConsumed() {
        _voteCompletedEvent.value = null
    }

    /** Toast 표시 후 이벤트 소비 */
    fun onAlreadyVotedEventConsumed() {
        _alreadyVotedEvent.value = false
    }
}

/**
 * Context가 필요한 ViewModel을 생성하기 위한 Factory.
 * (Android ViewModel은 기본 생성자만 지원하므로 Factory 필요)
 */
class ParticipationViewModelFactory(private val context: Context) :
    androidx.lifecycle.ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParticipationViewModel::class.java)) {
            return ParticipationViewModel(context.applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
