package com.siheung.seemoney.domain.vote.service;

import com.siheung.seemoney.domain.vote.dto.*;
import com.siheung.seemoney.domain.vote.entity.*;
import com.siheung.seemoney.domain.vote.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteParticipantRepository voteParticipantRepository;

    // =========================
    // 투표 생성
    // =========================

    @Transactional
    public void createVote(
            VoteCreateRequest request
    ) {

        Vote vote = Vote.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .endDate(request.getEndDate())
                .participantCount(0)
                .build();

        voteRepository.save(vote);

        // 선택지 저장
        for (String option : request.getOptions()) {

            VoteOption voteOption =
                    VoteOption.builder()
                            .vote(vote)
                            .optionName(option)
                            .voteCount(0)
                            .build();

            voteOptionRepository.save(voteOption);
        }
    }

    // =========================
    // 전체 투표 목록 조회
    // =========================

    public List<VoteListResponse> getVotes() {

        List<Vote> votes =
                voteRepository.findAll();

        List<VoteListResponse> result =
                new ArrayList<>();

        for (Vote vote : votes) {

            VoteListResponse response =
                    VoteListResponse.builder()
                            .voteId(vote.getVoteId())
                            .title(vote.getTitle())
                            .participantCount(vote.getParticipantCount())
                            .endDate(vote.getEndDate())
                            .build();

            result.add(response);
        }

        return result;
    }

    // =========================
    // 투표 상세 조회
    // =========================

    public VoteDetailResponse getVoteDetail(
            Long voteId
    ) {

        Vote vote =
                voteRepository.findById(voteId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("투표 없음"));

        List<VoteOption> options =
                voteOptionRepository.findByVote(vote);

        List<VoteOptionResponse> optionResponses =
                new ArrayList<>();

        for (VoteOption option : options) {

            double percent = 0;

            if (vote.getParticipantCount() > 0) {

                percent =
                        (double) option.getVoteCount()
                                / vote.getParticipantCount()
                                * 100;
            }

            VoteOptionResponse response =
                    VoteOptionResponse.builder()
                            .optionId(option.getOptionId())
                            .optionName(option.getOptionName())
                            .voteCount(option.getVoteCount())
                            .percent(percent)
                            .build();

            optionResponses.add(response);
        }

        return VoteDetailResponse.builder()
                .voteId(vote.getVoteId())
                .title(vote.getTitle())
                .description(vote.getDescription())
                .participantCount(vote.getParticipantCount())
                .endDate(vote.getEndDate())
                .options(optionResponses)
                .build();
    }
}