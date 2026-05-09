package com.siheung.seemoney.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    // 로그인 이메일
    private String email;

    // 원본 비밀번호
    private String password;
}