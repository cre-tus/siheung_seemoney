package com.siheung.seemoney.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    // API 인증용 JWT
    private String accessToken;

    // accessToken 재발급용
    private String refreshToken;

    // 사용자 PK
    private Long userId;

    // 사용자 이메일
    private String email;

    // 사용자 권한
    private String role;
}