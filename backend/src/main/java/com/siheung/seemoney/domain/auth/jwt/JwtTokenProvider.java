package com.siheung.seemoney.domain.auth.jwt;

import com.siheung.seemoney.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // application.properties의 jwt.secret 값 주입
    @Value("${jwt.secret}")
    private String secret;

    // JWT 암호화용 Key 객체
    private SecretKey key;

    // accessToken 만료시간
    private final long ACCESS_EXPIRE =
            1000L * 60 * 60;

    // refreshToken 만료시간
    private final long REFRESH_EXPIRE =
            1000L * 60 * 60 * 24 * 14;

    // 객체 생성 후 자동 실행
    @PostConstruct
    public void init() {

        // 문자열 secret을 JWT Key 객체로 변환
        this.key =
                Keys.hmacShaKeyFor(
                        secret.getBytes(StandardCharsets.UTF_8)
                );
    }

    // AccessToken 생성
    public String createAccessToken(User user) {

        Date now = new Date();

        Date expireDate =
                new Date(now.getTime() + ACCESS_EXPIRE);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getUserId())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(User user) {

        Date now = new Date();

        Date expireDate =
                new Date(now.getTime() + REFRESH_EXPIRE);

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }

    // JWT에서 이메일 추출
    public String getEmail(String token) {

        return getClaims(token).getSubject();
    }

    // JWT 유효성 검사
    public boolean validateToken(String token) {

        try {

            getClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    // JWT Claims 추출
    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}