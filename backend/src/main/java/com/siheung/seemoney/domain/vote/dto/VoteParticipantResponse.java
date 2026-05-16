package com.siheung.seemoney.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class VoteParticipantResponse {

    // 사용자 이메일
    private String email;

    // 선택한 항목
    private String selectedOption;

    // 참여 시간
    private LocalDateTime votedAt;
}