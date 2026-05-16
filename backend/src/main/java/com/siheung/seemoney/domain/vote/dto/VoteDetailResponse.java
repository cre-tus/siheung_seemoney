package com.siheung.seemoney.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class VoteDetailResponse {

    // 투표 ID
    private Long voteId;

    // 제목
    private String title;

    // 설명
    private String description;

    // 참여자 수
    private Integer participantCount;

    // 마감일
    private LocalDate endDate;

    // 선택지 목록
    private List<VoteOptionResponse> options;
}