package com.siheung.seemoney.domain.activity.controller;

import com.siheung.seemoney.domain.activity.dto.ActivityRequestDto;
import com.siheung.seemoney.domain.activity.dto.ActivityResponseDto;
import com.siheung.seemoney.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;

    /**
     * 활동 기록 생성
     * POST /api/activities
     */
    @PostMapping
    public ResponseEntity<ActivityResponseDto> createActivity(@RequestBody ActivityRequestDto requestDto) {
        return ResponseEntity.ok(activityService.createActivity(requestDto));
    }

    /**
     * 특정 유저의 활동 내역 조회
     * GET /api/activities/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponseDto>> getUserActivities(@PathVariable Long userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }
}
