package com.siheung.seemoney.domain.post.repository;

import com.siheung.seemoney.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 게시글 리포지토리
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    // publicId로 게시글 조회
    Optional<Post> findByPublicId(String publicId);
}
