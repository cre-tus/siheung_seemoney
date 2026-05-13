package com.siheung.seemoney.domain.post.service;

import com.siheung.seemoney.domain.post.dto.PostResponseDto;
import com.siheung.seemoney.domain.post.entity.Post;
import com.siheung.seemoney.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final com.siheung.seemoney.domain.user.repository.UserRepository userRepository;

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto createPost(com.siheung.seemoney.domain.post.dto.PostRequestDto requestDto) {
        // 임시: 인증 시스템 연동 전까지 ID 1번 유저를 작성자로 설정
        com.siheung.seemoney.domain.user.entity.User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default user (ID 1) not found. Please run insert.sql"));

        Post post = Post.builder()
                .user(user)
                .nickname("시흥시민") // 임시 닉네임
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
        
        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }
}
