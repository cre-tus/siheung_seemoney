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

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPostByPublicId(String publicId) {
        Post post = postRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post publicId: " + publicId));
        return new PostResponseDto(post);
    }
}
