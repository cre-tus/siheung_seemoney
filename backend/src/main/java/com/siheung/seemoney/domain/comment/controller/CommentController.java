package com.siheung.seemoney.domain.comment.controller;

import com.siheung.seemoney.domain.comment.dto.CommentRequestDto;
import com.siheung.seemoney.domain.comment.dto.CommentResponseDto;
import com.siheung.seemoney.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    /**
     * [DB 저장 로직 설명]
     * 1. 특정 게시글 ID를 경로(PathVariable)로 받고, 댓글 내용은 Body로 받음
     *    POST /api/comments/post/1
     *    Body: { "content": "댓글 내용" }
     * 2. 서비스 레이어에서 해당 postId를 가진 Post를 찾고, 새로운 Comment 객체를 생성
     * 3. DB의 comments 테이블에 한 줄(Row)이 추가됨
     */
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.createComment(postId, requestDto));
    }

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
