package com.siheung.seemoney.domain.post.controller;

import com.siheung.seemoney.domain.post.dto.PostResponseDto;
import com.siheung.seemoney.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    /**
     * 전체 게시글 목록 조회
     * GET /api/posts
     * 
     * [응답 JSON 예시]
     * [
     *   {
     *     "id": 1,
     *     "publicId": "uuid-string",
     *     "nickname": "작성자닉네임",
     *     "title": "게시글 제목",
     *     "content": "게시글 내용...",
     *     "likeCount": 5,
     *     "commentCount": 3,
     *     "viewCount": 50,
     *     "createdAt": "2026-05-13T10:00:00"
     *   }
     * ]
     */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    /**
     * 게시글 상세 조회 (publicId 사용)
     * GET /api/posts/{publicId}
     * 
     * [응답 JSON 예시]
     * {
     *   "id": 1,
     *   "publicId": "uuid-string",
     *   "nickname": "작성자닉네임",
     *   "title": "게시글 제목",
     *   "content": "게시글 내용...",
     *   "likeCount": 5,
     *   "commentCount": 3,
     *   "viewCount": 50,
     *   "createdAt": "2026-05-13T10:00:00"
     * }
     */
    @GetMapping("/{publicId}")
    public ResponseEntity<PostResponseDto> getPostByPublicId(@PathVariable String publicId) {
        return ResponseEntity.ok(postService.getPostByPublicId(publicId));
    }
}
