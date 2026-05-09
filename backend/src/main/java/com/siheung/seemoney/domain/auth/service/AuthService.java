package com.siheung.seemoney.domain.auth.service;

import com.siheung.seemoney.domain.auth.dto.LoginRequest;
import com.siheung.seemoney.domain.auth.dto.LoginResponse;
import com.siheung.seemoney.domain.auth.dto.MeResponse;
import com.siheung.seemoney.domain.auth.dto.SignupRequest;
import com.siheung.seemoney.domain.auth.jwt.JwtTokenProvider;
import com.siheung.seemoney.domain.user.entity.User;
import com.siheung.seemoney.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // 서비스 계층 Bean 등록
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class AuthService {

    // users 테이블 접근
    private final UserRepository userRepository;

    // BCrypt 비밀번호 암호화/검증
    private final PasswordEncoder passwordEncoder;

    // JWT 생성/검증 클래스
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    public void signup(SignupRequest request) {

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        // 원본 비밀번호를 BCrypt 해시로 변환
        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(encodedPassword)
                .address(request.getAddress())
                .point(0)
                .userGrade("BRONZE")
                .voteCount(0)
                .proposalCount(0)
                .rankingPoint(0)
                .role("USER")
                .build();

        // DB 저장
        userRepository.save(user);
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {

        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("이메일 또는 비밀번호가 틀렸습니다.")
                );

        // 입력 비밀번호와 DB의 passwordHash 비교
        boolean isPasswordMatch =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPasswordHash()
                );

        // 비밀번호 틀리면 예외
        if (!isPasswordMatch) {
            throw new RuntimeException("이메일 또는 비밀번호가 틀렸습니다.");
        }

        // AccessToken 생성
        String accessToken =
                jwtTokenProvider.createAccessToken(user);

        // RefreshToken 생성
        String refreshToken =
                jwtTokenProvider.createRefreshToken(user);

        // 로그인 응답 반환
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    // 현재 로그인한 사용자 정보 조회
    public MeResponse getMe(User user) {

        return MeResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .address(user.getAddress())
                .point(user.getPoint())
                .userGrade(user.getUserGrade())
                .voteCount(user.getVoteCount())
                .proposalCount(user.getProposalCount())
                .rankingPoint(user.getRankingPoint())
                .role(user.getRole())
                .build();
    }
}