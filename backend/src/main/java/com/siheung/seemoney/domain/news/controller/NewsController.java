package com.siheung.seemoney.domain.news.controller;

import com.siheung.seemoney.domain.news.dto.NewsDto;
import com.siheung.seemoney.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 뉴스 API 엔드포인트 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * 시흥시 재정/경제 뉴스 리스트 조회 (네이버 실시간 연동)
     * GET /api/v1/news
     * 
     * [응답 JSON 예시]
     * [
     *   {
     *     "title": "시흥시 예산 확정...",
     *     "link": "https://news.naver.com/...",
     *     "summary": "뉴스 본문 요약...",
     *     "pubDate": "Wed, 13 May 2026 10:00:00 +0900"
     *   }
     * ]
     */
    @GetMapping
    public ResponseEntity<List<NewsDto.NewsResponse>> getNews() {
        List<NewsDto.NewsResponse> newsList = newsService.getSiheungEconomyNews();
        return ResponseEntity.ok(newsList);
    }
}
