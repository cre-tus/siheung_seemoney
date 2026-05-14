package com.siheung.seemoney.domain.activity.entity;

import com.siheung.seemoney.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 활동 내역 엔티티
 * 유저가 수행한 활동(투표, 글 작성 등)을 기록합니다.
 */
@Entity
@Table(name = "activities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 활동한 사용자 (다대일 관계, 지연 로딩)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 활동 타입 (VOTE, POST, LIKE, COMMENT)
    @Column(nullable = false, length = 20)
    private String type;

    // 활동 제목 (로그 메시지)
    @Column(nullable = false, length = 255)
    private String title;

    // 획득/차감 포인트
    @Builder.Default
    @Column(nullable = false)
    private int points = 0;

    // 연결된 게시글 ID (물리적 FK는 생략 가능)
    @Column(name = "post_id")
    private Long postId;

    // 연결된 게시글 UUID
    @Column(name = "post_public_id", length = 36)
    private String postPublicId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
