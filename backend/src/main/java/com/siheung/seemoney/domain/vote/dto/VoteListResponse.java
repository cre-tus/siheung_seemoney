package com.siheung.seemoney.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class VoteListResponse {

    // 투표 ID
    private Long voteId;

    // 제목
    private String title;

    // 참여자 수
    private Integer participantCount;

    // 마감일
    private LocalDate endDate;
}