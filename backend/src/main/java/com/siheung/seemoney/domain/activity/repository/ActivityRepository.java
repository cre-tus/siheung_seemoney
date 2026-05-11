package com.siheung.seemoney.domain.activity.repository;

import com.siheung.seemoney.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 활동 내역 리포지토리
 */
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

}
