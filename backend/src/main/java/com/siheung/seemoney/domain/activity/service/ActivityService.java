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

    public List<ActivityResponseDto> getUserActivities(Long userId) {
        return activityRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(ActivityResponseDto::new)
                .collect(Collectors.toList());
    }
}
