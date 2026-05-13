package com.siheung.seemoney.config;

import com.siheung.seemoney.domain.auth.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        // JWT 인증 필터
        private final JwtAuthFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain filterChain(
                        HttpSecurity http) throws Exception {

                http

                                // csrf 비활성화
                                // JWT 기반 REST API에서는 사용 안 함
                                .csrf(csrf -> csrf.disable())

                                // 세션 사용 안 함
                                // JWT 방식은 Stateless
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                // URL 접근 권한 설정
                                .authorizeHttpRequests(auth -> auth

                                                // 로그인/회원가입은 인증 없이 허용
                                                .requestMatchers(
                                                                "/api/auth/login",
                                                                "/api/auth/signup")
                                                .permitAll()

                                                // 예산 API는 인증 없이 허용 (프론트 테스트용)
                                                .requestMatchers("/api/budgets/**").permitAll()
                                                .requestMatchers("/api/v1/news/**").permitAll()

                                                // 나머지는 로그인 필요
                                                .anyRequest().authenticated())

                                // JWT 필터 등록
                                // UsernamePasswordAuthenticationFilter 전에 실행
                                .addFilterBefore(
                                                jwtAuthFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // BCrypt 암호화 객체 Bean 등록
        @Bean
        public PasswordEncoder passwordEncoder() {

                return new BCryptPasswordEncoder();
        }
}