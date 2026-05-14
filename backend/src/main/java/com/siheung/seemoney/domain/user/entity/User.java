package com.siheung.seemoney.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // JPA 엔티티 지정
@Table(name = "users") // 매핑할 테이블명
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // users 테이블 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 이메일 (로그인 ID)
    @Column(nullable = false, unique = true)
    private String email;

    // BCrypt 암호화 비밀번호
    @Column(nullable = false)
    private String passwordHash;

    // 주소
    @Column(nullable = false)
    private String address;

    // 사용자 포인트
    @Builder.Default
    private Integer point = 0;

    // 사용자 등급
    @Builder.Default
    private String userGrade = "BRONZE";

    // 투표 횟수
    @Builder.Default
    private Integer voteCount = 0;

    // 제안 횟수
    @Builder.Default
    private Integer proposalCount = 0;

    // 랭킹 점수
    @Builder.Default
    private Integer rankingPoint = 0;

    // USER / ADMIN 권한
    @Builder.Default
    private String role = "USER";

    // 생성 시간
    private LocalDateTime createdAt;

    // 수정 시간
    private LocalDateTime updatedAt;

    // INSERT 직전에 자동 실행
    @PrePersist
    public void prePersist() {

        // 생성 시간 저장
        this.createdAt = LocalDateTime.now();

        // 수정 시간 저장
        this.updatedAt = LocalDateTime.now();
    }

    // UPDATE 직전에 자동 실행
    @PreUpdate
    public void preUpdate() {

        // 수정 시간 갱신
        this.updatedAt = LocalDateTime.now();
    }
}