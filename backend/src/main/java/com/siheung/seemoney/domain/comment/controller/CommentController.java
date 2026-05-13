package com.siheung.seemoney.domain.comment.controller;

import com.siheung.seemoney.domain.comment.dto.CommentResponseDto;
import com.siheung.seemoney.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    /**
     * 특정 게시글의 댓글 목록 조회
     * GET /api/comments/post/{postId}
     * 
     * [응답 JSON 예시]
     * [
     *   {
     *     "id": 1,
     *     "nickname": "작성자닉네임",
     *     "content": "댓글 내용입니다.",
     *     "createdAt": "2026-05-13T12:00:00"
     *   }
     * ]
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
}
