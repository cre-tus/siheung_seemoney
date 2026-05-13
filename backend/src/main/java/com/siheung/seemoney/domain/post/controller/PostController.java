package com.siheung.seemoney.domain.post.controller;

import com.siheung.seemoney.domain.post.dto.PostRequestDto;
import com.siheung.seemoney.domain.post.dto.PostResponseDto;
import com.siheung.seemoney.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    /**
     * [DB 저장 로직 설명]
     * 1. 프론트엔드에서 아래 JSON 구조로 POST 요청을 보냄
     *    { "title": "제목", "content": "내용" }
     * 2. @RequestBody를 통해 JSON이 PostRequestDto 객체로 변환됨
     * 3. 서비스(Service)에서 DTO 데이터를 Post 엔티티로 변환
     * 4. 리포지토리(Repository)의 .save()를 통해 실제 DB(MySQL/SQLite)에 저장됨
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto) {
        return ResponseEntity.ok(postService.createPost(requestDto));
    }

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
