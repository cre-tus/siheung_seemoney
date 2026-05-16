package com.siheung.seemoney.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeResponse {

    // 사용자 PK
    private Long userId;

    // 이메일
    private String email;

    // 주소
    private String address;

    //닉네임
    private String nickname;

    // 포인트
    private Integer point;

    // 등급
    private String userGrade;

    // 투표 수
    private Integer voteCount;

    // 제안 수
    private Integer proposalCount;

    // 랭킹 점수
    private Integer rankingPoint;

    // 권한
    private String role;
}