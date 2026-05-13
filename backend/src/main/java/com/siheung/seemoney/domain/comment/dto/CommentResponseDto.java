package com.siheung.seemoney.domain.comment.dto;

import com.siheung.seemoney.domain.comment.entity.Comment;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
