package com.example.siheung_seemoney.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityParticipateBinding
import com.example.siheung_seemoney.ui.adapter.PollAdapter
import com.example.siheung_seemoney.view_model.ParticipationViewModel
import com.example.siheung_seemoney.view_model.ParticipationViewModelFactory
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast

/**
 * 시민 참여 화면 — 투표 기능.
 *
 * [현재] PollRepository의 목업 데이터 + SharedPreferences 로컬 투표 상태.
 * [추후 API 연동 시]
 *   1. PollRepository.getPolls() 를 Retrofit GET /api/polls 로 교체.
 *   2. PollRepository.vote()    를 Retrofit POST /api/polls/{id}/vote 로 교체.
 *   3. vote() 호출 시 JWT 토큰을 Authorization 헤더로 전달.
 *   4. 백엔드에서 users.vote_count += 1 자동 처리 (PollRepository 주석 참고).
 *
 * [로그인 요구사항] JWT 토큰이 없으면 LoginActivity로 리다이렉트.
 * SecurityConfig에 투표 API 추가 시 인증 필요 엔드포인트로 설정 필요.
 */
class ParticipateActivity : BaseActivity<ActivityParticipateBinding>() {

    override fun inflateBinding(): ActivityParticipateBinding {
        return ActivityParticipateBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: ParticipationViewModel
    private lateinit var pollAdapter: PollAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── 로그인 체크: JWT 토큰 없으면 로그인 화면으로 ──
        // [추후 API 연동 시] 이 토큰을 PollRepository.vote() 호출 시 헤더로 전달
        val token = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getString("jwt_token", null)

        if (token == null) {
            Toast.makeText(this, "투표에 참여하려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupViewModel()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupViewModel() {
        val factory = ParticipationViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[ParticipationViewModel::class.java]
    }

    private fun setupRecyclerView() {
        pollAdapter = PollAdapter { poll, optionIndex ->
            viewModel.vote(poll, optionIndex)
        }

        binding.rvPolls.apply {
            layoutManager = LinearLayoutManager(this@ParticipateActivity)
            adapter = pollAdapter
            setHasFixedSize(false)
        }
    }

    private fun observeViewModel() {
        // 투표 항목 리스트 관찰
        viewModel.polls.observe(this) { polls ->
            pollAdapter.submitPolls(polls)
        }

        // 투표 상태 전체 관찰 (초기 로드 및 갱신)
        viewModel.voteStates.observe(this) { states ->
            pollAdapter.submitVoteStates(states)
        }

        // 투표 완료 이벤트 → SnackBar 표시
        viewModel.voteCompletedEvent.observe(this) { pollId ->
            if (pollId != null) {
                Snackbar.make(
                    binding.root,
                    "✓ 의견이 성공적으로 반영되었습니다. (+100P)",
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.onVoteCompletedEventConsumed()
            }
        }

        // 중복 투표 시도 → Toast 표시
        viewModel.alreadyVotedEvent.observe(this) { alreadyVoted ->
            if (alreadyVoted == true) {
                Toast.makeText(this, "이미 참여한 투표입니다.", Toast.LENGTH_SHORT).show()
                viewModel.onAlreadyVotedEventConsumed()
            }
        }
    }
}