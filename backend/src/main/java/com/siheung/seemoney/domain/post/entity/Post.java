package com.siheung.seemoney.domain.post.entity;

import com.siheung.seemoney.domain.comment.entity.Comment;
import com.siheung.seemoney.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 게시글 엔티티
 * 게시글 정보를 담고 있으며 댓글과 1:N 관계를 가집니다.
 */
@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 외부 노출용 UUID
    @Column(name = "public_id", nullable = false, unique = true, length = 36)
    private String publicId;

    // 작성자 (User 엔티티와 다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 작성 당시 닉네임 (탈퇴나 변경 대비 스냅샷 용도)
    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder.Default
    @Column(name = "like_count")
    private int likeCount = 0;

    @Builder.Default
    @Column(name = "comment_count")
    private int commentCount = 0;

    @Builder.Default
    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 게시글에 달린 댓글 리스트 (1:N 양방향 관계, 지연 로딩)
    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.publicId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 비즈니스 로직: 게시글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 조회수 증가
    public void incrementViewCount() {
        this.viewCount++;
    }
}
