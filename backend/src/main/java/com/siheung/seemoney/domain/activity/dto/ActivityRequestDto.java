package com.siheung.seemoney.domain.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequestDto {
    private String type;         // VOTE, POST, LIKE, COMMENT 등
    private String title;        // 활동 설명 (예: "학교 급식 예산안에 투표했습니다")
    private int points;          // 획득한 포인트
    private Long postId;         // 관련 게시글 ID (선택)
    private String postPublicId; // 관련 게시글 UUID (선택)
}
