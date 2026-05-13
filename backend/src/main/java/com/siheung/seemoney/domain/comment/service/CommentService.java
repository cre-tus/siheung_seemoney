package com.siheung.seemoney.domain.comment.service;

import com.siheung.seemoney.domain.comment.dto.CommentResponseDto;
import com.siheung.seemoney.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final com.siheung.seemoney.domain.post.repository.PostRepository postRepository;
    private final com.siheung.seemoney.domain.user.repository.UserRepository userRepository;

    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto createComment(Long postId, com.siheung.seemoney.domain.comment.dto.CommentRequestDto requestDto) {
        // 임시: 인증 시스템 연동 전까지 ID 1번 유저를 작성자로 설정
        com.siheung.seemoney.domain.user.entity.User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default user (ID 1) not found. Please run insert.sql"));

        com.siheung.seemoney.domain.post.entity.Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));
        
        com.siheung.seemoney.domain.comment.entity.Comment comment = com.siheung.seemoney.domain.comment.entity.Comment.builder()
                .post(post)
                .user(user)
                .nickname("시흥시민")
                .content(requestDto.getContent())
                .build();
        
        return new CommentResponseDto(commentRepository.save(comment));
    }
}
