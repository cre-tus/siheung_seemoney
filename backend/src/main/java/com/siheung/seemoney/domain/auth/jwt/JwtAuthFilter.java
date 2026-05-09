package com.siheung.seemoney.domain.auth.jwt;

import com.siheung.seemoney.domain.user.entity.User;
import com.siheung.seemoney.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // JWT 기능 클래스
    private final JwtTokenProvider jwtTokenProvider;

    // 사용자 조회 Repository
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Authorization 헤더 추출
        String authHeader =
                request.getHeader("Authorization");

        // Bearer 토큰 형식인지 확인
        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            // "Bearer " 제거
            String token = authHeader.substring(7);

            // 토큰 유효성 검사
            if (jwtTokenProvider.validateToken(token)) {

                // JWT 내부 이메일 추출
                String email =
                        jwtTokenProvider.getEmail(token);

                // DB 사용자 조회
                User user =
                        userRepository.findByEmail(email)
                                .orElse(null);

                // 사용자 존재 시 인증 처리
                if (user != null) {

                    // Spring Security 인증 객체 생성
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(

                                    // 로그인 사용자 객체
                                    user,

                                    // 비밀번호는 이미 인증 완료 상태
                                    null,

                                    // 사용자 권한
                                    List.of(
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + user.getRole()
                                            )
                                    )
                            );

                    // 현재 요청에 인증 정보 저장
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(auth);
                }
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}