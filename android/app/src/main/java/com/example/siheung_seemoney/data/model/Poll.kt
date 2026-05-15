package com.example.siheung_seemoney.data.model

import com.google.gson.annotations.SerializedName

/**
 * 시민 투표 항목 데이터 모델.
 *
 * [현재] 목업 데이터 기반으로 동작.
 * [추후 API 연동 시] 아래 필드는 백엔드 polls 테이블 스키마와 맞춰서 교체.
 * - id: polls.id
 * - title: polls.title
 * - description: polls.description
 * - options: polls.options (JSON Array)
 * - isActive: polls.is_active
 * - endsAt: polls.ends_at
 */
data class Poll(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("options")
    val options: List<String>,

    @SerializedName("is_active")
    val isActive: Boolean = true,

    @SerializedName("ends_at")
    val endsAt: String? = null,

    // 현재 총 참여자 수 (서버에서 집계된 값)
    // [추후 API 연동 시] 백엔드 poll_votes 테이블의 COUNT 집계값으로 대체
    @SerializedName("total_votes")
    val totalVotes: Int = 0
)

/**
 * 특정 투표 항목에 대한 현재 사용자의 투표 상태 및 집계 결과.
 *
 * - selectedOption: 사용자가 선택한 옵션 인덱스 (null이면 아직 미투표)
 * - voteCounts: 각 선택지별 누적 투표 수
 *
 * [현재] SharedPreferences에 저장하여 로컬 관리.
 * [추후 API 연동 시] GET /api/polls/{id}/results 응답으로 대체.
 */
data class PollVoteState(
    val pollId: Int,
    val selectedOption: Int? = null,
    val voteCounts: List<Int>
) {
    /** 전체 투표 수 */
    val total: Int get() = voteCounts.sum()

    /** 특정 옵션의 퍼센트 (0~100, 소수점 1자리) */
    fun percentOf(index: Int): Float {
        if (total == 0) return 0f
        return (voteCounts[index].toFloat() / total) * 100f
    }

    /** 이미 투표했는지 여부 */
    val hasVoted: Boolean get() = selectedOption != null
}
