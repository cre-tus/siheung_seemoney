package com.siheung.seemoney.domain.news.service;

import com.siheung.seemoney.domain.news.dto.NewsDto;
import com.siheung.seemoney.infra.naver.NaverNewsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 뉴스 관련 비즈니스 로직 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NaverNewsClient naverNewsClient;

    /**
     * 시흥시 재정 및 예산 관련 뉴스를 가져와 DTO로 변환하여 반환
     */
    public List<NewsDto.NewsResponse> getSiheungEconomyNews() {
        // 기본 검색어 설정
        String query = "시흥시 재정 OR 시흥시 예산";
        
        NewsDto.NaverNewsResponse naverResponse = naverNewsClient.searchNews(query);
        
        if (naverResponse == null || naverResponse.getItems() == null) {
            return new ArrayList<>();
        }

        return NewsDto.of(naverResponse);
    }
}
