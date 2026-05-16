package com.siheung.seemoney.domain.vote.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteParticipateRequest {

    // 선택한 항목 ID
    private Long optionId;
}