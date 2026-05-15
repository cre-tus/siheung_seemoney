package com.example.siheung_seemoney.ui.adapter

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.siheung_seemoney.R
import com.example.siheung_seemoney.data.model.Poll
import com.example.siheung_seemoney.data.model.PollVoteState
import com.example.siheung_seemoney.databinding.ItemPollBinding

/**
 * 시민 참여 투표 항목 RecyclerView 어댑터.
 *
 * [투표 전] 선택지 버튼 목록 표시 (viewVotingPanel)
 * [투표 후] 결과 ProgressBar + 퍼센트 표시 (viewResultPanel) — 애니메이션 포함
 */
class PollAdapter(
    private val onVoteClick: (poll: Poll, optionIndex: Int) -> Unit
) : RecyclerView.Adapter<PollAdapter.PollViewHolder>() {

    private val polls = mutableListOf<Poll>()
    private val voteStates = mutableMapOf<Int, PollVoteState>()

    fun submitPolls(list: List<Poll>) {
        polls.clear()
        polls.addAll(list)
        notifyDataSetChanged()
    }

    fun updateVoteState(state: PollVoteState) {
        voteStates[state.pollId] = state
        val idx = polls.indexOfFirst { it.id == state.pollId }
        if (idx >= 0) notifyItemChanged(idx)
    }

    fun submitVoteStates(states: Map<Int, PollVoteState>) {
        voteStates.clear()
        voteStates.putAll(states)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollViewHolder {
        val binding = ItemPollBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PollViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PollViewHolder, position: Int) {
        val poll = polls[position]
        val state = voteStates[poll.id]
        holder.bind(poll, state)
    }

    override fun getItemCount(): Int = polls.size

    inner class PollViewHolder(private val binding: ItemPollBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(poll: Poll, state: PollVoteState?) {
            // ── 공통 정보 바인딩 ──
            binding.tvPollTitle.text = poll.title
            binding.tvPollDescription.text = poll.description
            binding.tvPollDeadline.text = if (poll.endsAt != null) "마감: ${poll.endsAt}" else ""
            binding.tvPollParticipants.text =
                "${formatCount(state?.total ?: poll.totalVotes)}명 참여"

            if (state?.hasVoted == true) {
                showResultPanel(poll, state)
            } else {
                showVotingPanel(poll)
            }
        }

        // ─────────────────────────────────────────────────────────
        // 투표 전 패널
        // ─────────────────────────────────────────────────────────
        private fun showVotingPanel(poll: Poll) {
            binding.viewVotingPanel.visibility = View.VISIBLE
            binding.viewResultPanel.visibility = View.GONE

            val ctx = binding.root.context
            val llOptions = binding.llOptions
            llOptions.removeAllViews()

            // 현재 선택된 옵션 인덱스 (라디오 버튼 효과 구현용)
            var selectedIndex = -1
            val optionViews = mutableListOf<TextView>()

            poll.options.forEachIndexed { idx, optionText ->
                val optionBtn = TextView(ctx).apply {
                    text = optionText
                    textSize = 14f
                    setTextColor(Color.parseColor("#374151"))
                    // 프로젝트 커스텀 폰트 적용
                    typeface = ResourcesCompat.getFont(ctx, R.font.paperlogy_5medium)
                    background = ContextCompat.getDrawable(ctx, R.drawable.bg_card_border)
                    setPadding(40, 0, 40, 0)
                    gravity = android.view.Gravity.CENTER_VERTICAL
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 120
                    )
                    params.setMargins(0, 0, 0, 16)
                    layoutParams = params

                    setOnClickListener {
                        selectedIndex = idx
                        // 모든 버튼 초기화
                        optionViews.forEach { btn ->
                            btn.background = ContextCompat.getDrawable(ctx, R.drawable.bg_card_border)
                            btn.setTextColor(Color.parseColor("#374151"))
                            btn.setTypeface(null, Typeface.NORMAL)
                        }
                        // 선택된 버튼 하이라이트
                        this.background = ContextCompat.getDrawable(ctx, R.drawable.bg_button_outline)
                        this.setTextColor(Color.parseColor("#2563EB"))
                        this.setTypeface(null, Typeface.BOLD)
                    }
                }
                optionViews.add(optionBtn)
                llOptions.addView(optionBtn)
            }

            // 투표하기 버튼
            binding.btnSubmitVote.setOnClickListener {
                if (selectedIndex >= 0) {
                    onVoteClick(poll, selectedIndex)
                }
            }
        }

        // ─────────────────────────────────────────────────────────
        // 투표 후 결과 패널
        // ─────────────────────────────────────────────────────────
        private fun showResultPanel(poll: Poll, state: PollVoteState) {
            binding.viewVotingPanel.visibility = View.GONE
            binding.viewResultPanel.visibility = View.VISIBLE

            val myOption = state.selectedOption ?: return
            binding.tvMyVoteLabel.text = "✓  내 선택: ${poll.options[myOption]}"

            val ctx = binding.root.context
            val llResults = binding.llResults
            llResults.removeAllViews()

            poll.options.forEachIndexed { idx, optionText ->
                val percent = state.percentOf(idx)
                val isMyChoice = idx == myOption

                // 옵션명 + 퍼센트 행
                val labelRow = LinearLayout(ctx).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                val tvLabel = TextView(ctx).apply {
                    text = if (isMyChoice) "✓  $optionText" else "   $optionText"
                    textSize = 13f
                    setTextColor(if (isMyChoice) Color.parseColor("#2563EB") else Color.parseColor("#374151"))
                    setTypeface(null, if (isMyChoice) Typeface.BOLD else Typeface.NORMAL)
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                }

                val tvPercent = TextView(ctx).apply {
                    text = "${"%.1f".format(percent)}%"
                    textSize = 13f
                    setTextColor(if (isMyChoice) Color.parseColor("#2563EB") else Color.parseColor("#6B7280"))
                    setTypeface(null, if (isMyChoice) Typeface.BOLD else Typeface.NORMAL)
                }

                labelRow.addView(tvLabel)
                labelRow.addView(tvPercent)

                // ProgressBar
                val progressBar = ProgressBar(ctx, null, android.R.attr.progressBarStyleHorizontal).apply {
                    max = 100
                    progress = 0  // 애니메이션을 위해 0으로 시작
                    val progressDrawableRes =
                        if (isMyChoice) R.drawable.bg_progress_blue else R.drawable.bg_progress
                    progressDrawable = ContextCompat.getDrawable(ctx, progressDrawableRes)
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 24
                    )
                    params.setMargins(0, 6, 0, 14)
                    layoutParams = params
                }

                llResults.addView(labelRow)
                llResults.addView(progressBar)

                // ProgressBar 애니메이션 (0 → 실제 퍼센트)
                progressBar.post {
                    ObjectAnimator.ofInt(progressBar, "progress", 0, percent.toInt()).apply {
                        duration = 600
                        startDelay = (idx * 100).toLong()
                        start()
                    }
                }
            }
        }

        /** 숫자를 3자리 콤마 포맷으로 반환 */
        private fun formatCount(count: Int): String {
            return String.format("%,d", count)
        }
    }
}
