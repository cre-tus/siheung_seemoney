package com.siheung.seemoney.domain.vote.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class VoteCreateRequest {

    // 투표 제목
    private String title;

    // 투표 설명
    private String description;

    // 마감일
    private LocalDate endDate;

    // 투표 선택지 목록
    private List<String> options;
}