package com.siheung.seemoney.domain.activity.controller;

import com.siheung.seemoney.domain.activity.dto.ActivityRequestDto;
import com.siheung.seemoney.domain.activity.dto.ActivityResponseDto;
import com.siheung.seemoney.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    /**
     * 유저의 활동 내역 조회
     * GET /api/activities
     */
    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> getActivities() {
        // 임시로 ID 1번 유저의 활동을 조회 (추후 JWT에서 추출하도록 변경)
        return ResponseEntity.ok(activityService.getUserActivities(1L));
    }

    /**
     * 새로운 활동 추가
     * POST /api/activities
     */
    @PostMapping
    public ResponseEntity<ActivityResponseDto> addActivity(@RequestBody ActivityRequestDto requestDto) {
        return ResponseEntity.ok(activityService.createActivity(requestDto));
    }
}