package com.siheung.seemoney.domain.post.dto;

import com.siheung.seemoney.domain.post.entity.Post;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private final Long id;
    private final String publicId;
    private final String nickname;
    private final String title;
    private final String content;
    private final int likeCount;
    private final int commentCount;
    private final int viewCount;
    private final LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.publicId = post.getPublicId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
    }
}
