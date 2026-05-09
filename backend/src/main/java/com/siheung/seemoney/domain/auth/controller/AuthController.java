package com.siheung.seemoney.domain.auth.controller;

import com.siheung.seemoney.domain.auth.dto.LoginRequest;
import com.siheung.seemoney.domain.auth.dto.LoginResponse;
import com.siheung.seemoney.domain.auth.dto.MeResponse;
import com.siheung.seemoney.domain.auth.dto.SignupRequest;
import com.siheung.seemoney.domain.auth.service.AuthService;
import com.siheung.seemoney.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController // REST API 컨트롤러
@RequestMapping("/api/auth") // 공통 URL prefix
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class AuthController {

    // 인증 관련 비즈니스 로직
    private final AuthService authService;

    // 회원가입 API
    // POST /api/auth/signup
    @PostMapping("/signup")
    public String signup(
            @RequestBody SignupRequest request
    ) {

        // 회원가입 처리
        authService.signup(request);

        // 임시 응답
        return "회원가입 성공";
    }

    // 로그인 API
    // POST /api/auth/login
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        // 로그인 성공 시 JWT 토큰 반환
        return authService.login(request);
    }

    // 내 정보 조회 API
    // GET /api/auth/me
    @GetMapping("/me")
    public MeResponse me(
            @AuthenticationPrincipal User user
    ) {

        // JwtAuthFilter에서 SecurityContext에 넣은 User를 사용
        return authService.getMe(user);
    }
}