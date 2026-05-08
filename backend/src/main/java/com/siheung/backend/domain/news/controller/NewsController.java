package com.siheung.backend.domain.news.controller;

import com.siheung.backend.domain.news.dto.NewsDto;
import com.siheung.backend.domain.news.service.NewsService;
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
     * 시흥시 재정/경제 뉴스 리스트 조회
     * GET /api/v1/news
     */
    @GetMapping
    public ResponseEntity<List<NewsDto.NewsResponse>> getNews() {
        List<NewsDto.NewsResponse> newsList = newsService.getSiheungEconomyNews();
        return ResponseEntity.ok(newsList);
    }
}
