package com.siheung.seemoney.domain.comment.repository;

import com.siheung.seemoney.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 댓글 리포지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 모든 댓글 조회
    List<Comment> findByPostId(Long postId);
}
