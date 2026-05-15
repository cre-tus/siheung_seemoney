package com.example.siheung_seemoney.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.siheung_seemoney.data.model.Poll
import com.example.siheung_seemoney.data.model.PollVoteState

/**
 * 시민 투표 데이터를 관리하는 Repository.
 *
 * [현재 Phase 1] 목업 데이터 + SharedPreferences 로컬 투표 상태 저장.
 * [추후 Phase 2 API 연동 시] 아래 두 함수만 교체하면 됨:
 *   - getPolls()       → Retrofit GET /api/polls
 *   - vote()           → Retrofit POST /api/polls/{id}/vote
 *   - getVoteState()   → Retrofit GET /api/polls/{id}/my-vote (또는 로컬 유지)
 *
 * SharedPreferences 키 규칙:
 *   - "voted_poll_{id}"        : 사용자가 선택한 옵션 인덱스 (-1 = 미투표)
 *   - "vote_count_{id}_{idx}"  : 각 선택지별 로컬 누적 투표 수
 */
class PollRepository(context: Context) {

    companion object {
        private const val PREFS_NAME = "poll_prefs"
        private const val KEY_VOTED_PREFIX = "voted_poll_"
        private const val KEY_COUNT_PREFIX = "vote_count_"
        private const val NOT_VOTED = -1

        /**
         * [참고: 백엔드 팀에게]
         * 투표 성공 시 users.vote_count 컬럼을 +1 해야 합니다.
         * 구현 위치: PollService.vote() 또는 PollVoteEventListener 등에서
         *   User user = userRepository.findById(userId);
         *   user.setVoteCount(user.getVoteCount() + 1);
         *   userRepository.save(user);
         * 또는 @Modifying 쿼리:
         *   UPDATE users SET vote_count = vote_count + 1 WHERE user_id = :userId
         */
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ─────────────────────────────────────────────────────────────
    // [추후 API 교체 포인트] getPolls(): 아래 목업 데이터를 제거하고
    // RetrofitClient.pollApiService.getPolls() 응답으로 교체.
    // ─────────────────────────────────────────────────────────────

    /**
     * 진행 중인 투표 항목 목록을 반환합니다.
     * 현재는 목업 데이터, 추후 백엔드 polls 테이블 연동으로 교체 예정.
     */
    fun getPolls(): List<Poll> = listOf(
        Poll(
            id = 1,
            title = "2027년 복지 예산 우선순위",
            description = "내년도 복지 예산에서 가장 우선적으로 투자해야 할 분야를 선택해주세요.",
            options = listOf("노인 복지", "아동·청소년 복지", "장애인 지원", "저소득층 지원"),
            isActive = true,
            endsAt = "2026.06.30",
            totalVotes = 2847
        ),
        Poll(
            id = 2,
            title = "교통 인프라 개선 방향",
            description = "시흥시 교통 개선을 위해 가장 필요한 사업은 무엇일까요?",
            options = listOf("버스 노선 확충", "자전거 도로 확대", "주차장 건설", "보행자 도로 정비"),
            isActive = true,
            endsAt = "2026.07.15",
            totalVotes = 3521
        ),
        Poll(
            id = 3,
            title = "환경 예산 집행 방향",
            description = "시흥시의 환경 예산을 어느 분야에 집중하는 것이 좋을까요?",
            options = listOf("공원·녹지 조성", "재활용 시설 확충", "미세먼지 저감", "하천 정비"),
            isActive = true,
            endsAt = "2026.08.01",
            totalVotes = 1930
        )
    )

    // ─────────────────────────────────────────────────────────────
    // 투표 상태 조회
    // ─────────────────────────────────────────────────────────────

    /**
     * 특정 투표 항목의 현재 상태를 반환합니다.
     * - selectedOption: 사용자가 선택한 옵션 (null이면 미투표)
     * - voteCounts: 각 선택지별 누적 투표 수 (로컬 집계)
     *
     * [추후 API 연동 시] GET /api/polls/{pollId}/results 로 대체 가능.
     * voteCounts는 서버 집계값으로 교체하고, selectedOption만 로컬 유지 가능.
     */
    fun getVoteState(poll: Poll): PollVoteState {
        val selectedOption = prefs.getInt("$KEY_VOTED_PREFIX${poll.id}", NOT_VOTED)
            .takeIf { it != NOT_VOTED }

        // 초기 목업 투표 분포 (실제 API 연동 시 서버에서 받아올 값)
        val initialDistribution = getMockInitialCounts(poll)

        val voteCounts = poll.options.indices.map { idx ->
            val localExtra = prefs.getInt("$KEY_COUNT_PREFIX${poll.id}_$idx", 0)
            initialDistribution[idx] + localExtra
        }

        return PollVoteState(
            pollId = poll.id,
            selectedOption = selectedOption,
            voteCounts = voteCounts
        )
    }

    // ─────────────────────────────────────────────────────────────
    // [추후 API 교체 포인트] vote(): 아래 로컬 저장 로직을 제거하고
    // RetrofitClient.pollApiService.vote(pollId, optionIndex, jwtToken) 으로 교체.
    // 성공 응답 받은 뒤 SharedPreferences selectedOption만 업데이트.
    // ─────────────────────────────────────────────────────────────

    /**
     * 투표를 처리합니다.
     *
     * @param poll 투표 항목
     * @param optionIndex 선택한 옵션 인덱스
     * @return 투표 성공 여부 (이미 투표한 경우 false)
     *
     * [추후 API 연동 시]
     * 1. POST /api/polls/{poll.id}/vote body: { option_index: optionIndex }
     * 2. 성공 시 prefs에 selectedOption 저장 (중복 방지용)
     * 3. 백엔드에서 users.vote_count += 1 처리 필요 (백엔드 팀 구현 필요)
     */
    fun vote(poll: Poll, optionIndex: Int): Boolean {
        val alreadyVoted = prefs.getInt("$KEY_VOTED_PREFIX${poll.id}", NOT_VOTED) != NOT_VOTED
        if (alreadyVoted) return false

        prefs.edit()
            .putInt("$KEY_VOTED_PREFIX${poll.id}", optionIndex)
            .putInt(
                "$KEY_COUNT_PREFIX${poll.id}_$optionIndex",
                prefs.getInt("$KEY_COUNT_PREFIX${poll.id}_$optionIndex", 0) + 1
            )
            .apply()

        return true
    }

    /**
     * 이미 투표했는지 확인합니다.
     */
    fun hasVoted(pollId: Int): Boolean {
        return prefs.getInt("$KEY_VOTED_PREFIX$pollId", NOT_VOTED) != NOT_VOTED
    }

    // ─────────────────────────────────────────────────────────────
    // 내부 헬퍼 — 실제 API 연동 시 제거
    // ─────────────────────────────────────────────────────────────

    /**
     * 현실감 있는 초기 목업 투표 분포를 반환합니다.
     * totalVotes 기준으로 각 선택지에 분배.
     * [추후 API 연동 시] 이 함수 전체 삭제 후 서버 집계값 사용.
     */
    private fun getMockInitialCounts(poll: Poll): List<Int> {
        val total = poll.totalVotes
        val n = poll.options.size
        return when (poll.id) {
            1 -> distributeMock(total, listOf(0.38f, 0.27f, 0.21f, 0.14f), n)
            2 -> distributeMock(total, listOf(0.53f, 0.27f, 0.12f, 0.08f), n)
            3 -> distributeMock(total, listOf(0.41f, 0.22f, 0.25f, 0.12f), n)
            else -> distributeMock(total, null, n)
        }
    }

    private fun distributeMock(total: Int, ratios: List<Float>?, n: Int): List<Int> {
        if (ratios == null || ratios.size != n) {
            val base = total / n
            return List(n) { base }
        }
        val counts = ratios.map { (it * total).toInt() }.toMutableList()
        // 반올림 오차 보정: 첫 번째 항목에 나머지 추가
        val diff = total - counts.sum()
        counts[0] += diff
        return counts
    }
}
