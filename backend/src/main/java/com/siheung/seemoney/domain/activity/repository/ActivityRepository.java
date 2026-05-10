package com.siheung.seemoney.domain.activity.repository;

import com.siheung.seemoney.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 활동 내역 리포지토리
 */
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    // 특정 사용자의 최신 활동 내역 조회
    List<Activity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
