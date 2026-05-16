package com.siheung.seemoney.domain.vote.controller;

import com.siheung.seemoney.domain.vote.dto.*;
import com.siheung.seemoney.domain.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    // =========================
    // 투표 생성
    // POST /api/votes
    // =========================

    @PostMapping
    public String createVote(
            @RequestBody VoteCreateRequest request
    ) {

        voteService.createVote(request);

        return "투표 생성 완료";
    }

    // =========================
    // 전체 투표 조회
    // GET /api/votes
    // =========================

    @GetMapping
    public List<VoteListResponse> getVotes() {

        return voteService.getVotes();
    }

    // =========================
    // 투표 상세 조회
    // GET /api/votes/{voteId}
    // =========================

    @GetMapping("/{voteId}")
    public VoteDetailResponse getVoteDetail(
            @PathVariable Long voteId
    ) {

        return voteService.getVoteDetail(voteId);
    }
}