package com.siheung.seemoney.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    // 회원가입 이메일
    private String email;

    // 원본 비밀번호
    private String password;

    // 사용자 주소
    private String address;

    // 사용자 닉네임
    private String nickname;
}