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
     * [DB 저장 로직 설명]
     * 1. 프론트엔드에서 활동 발생 시 아래 JSON 구조로 POST 요청
     *    { "type": "VOTE", "title": "설문에 참여했습니다", "points": 50 }
     * 2. 서비스 레이어에서 활동 기록을 생성하고 DB의 activities 테이블에 저장
     */
    @PostMapping
    public ResponseEntity<ActivityResponseDto> createActivity(@RequestBody ActivityRequestDto requestDto) {
        return ResponseEntity.ok(activityService.createActivity(requestDto));
    }

    /**
     * 특정 유저의 활동 내역 조회 (마이페이지용)
     * GET /api/activities/user/{userId}
     * 
     * [응답 JSON 예시]
     * [
     *   {
     *     "id": 1,
     *     "type": "POST", (활동 타입: POST, VOTE, LIKE, COMMENT)
     *     "title": "게시글을 작성했습니다.",
     *     "points": 10,
     *     "postId": 123,
     *     "postPublicId": "uuid-string",
     *     "createdAt": "2026-05-13T10:00:00"
     *   }
     * ]
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponseDto>> getUserActivities(@PathVariable Long userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }
}
