package com.siheung.seemoney.domain.activity.service;

import com.siheung.seemoney.domain.activity.dto.ActivityResponseDto;
import com.siheung.seemoney.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final com.siheung.seemoney.domain.user.repository.UserRepository userRepository;

    public List<ActivityResponseDto> getUserActivities(Long userId) {
        return activityRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(ActivityResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ActivityResponseDto createActivity(com.siheung.seemoney.domain.activity.dto.ActivityRequestDto requestDto) {
        // 임시: 인증 시스템 연동 전까지 ID 1번 유저를 활동 주체로 설정
        com.siheung.seemoney.domain.user.entity.User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default user (ID 1) not found."));

        com.siheung.seemoney.domain.activity.entity.Activity activity = com.siheung.seemoney.domain.activity.entity.Activity.builder()
                .user(user)
                .type(requestDto.getType())
                .title(requestDto.getTitle())
                .points(requestDto.getPoints())
                .postId(requestDto.getPostId())
                .postPublicId(requestDto.getPostPublicId())
                .build();

        return new ActivityResponseDto(activityRepository.save(activity));
    }
}
