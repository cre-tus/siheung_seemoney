package com.siheung.seemoney.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VoteOptionResponse {

    // 선택지 ID
    private Long optionId;

    // 선택지 이름
    private String optionName;

    // 득표 수
    private Integer voteCount;

    // 퍼센트
    private Double percent;
}