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
     * 시흥시 재정 및 예산 관련 뉴스를 따로따로 검색하여 교차(interleaving)하여 반환
     */
    public List<NewsDto.NewsResponse> getSiheungEconomyNews() {
        // 1. "시흥시 재정" 검색
        NewsDto.NaverNewsResponse financeResponse = naverNewsClient.searchNews("시흥시 재정");
        
        // 2. "시흥시 예산" 검색
        NewsDto.NaverNewsResponse budgetResponse = naverNewsClient.searchNews("시흥시 예산");
        
        List<NewsDto.NaverNewsItem> financeItems = (financeResponse != null && financeResponse.getItems() != null) 
                ? financeResponse.getItems() : new ArrayList<>();
        List<NewsDto.NaverNewsItem> budgetItems = (budgetResponse != null && budgetResponse.getItems() != null) 
                ? budgetResponse.getItems() : new ArrayList<>();

        List<NewsDto.NaverNewsItem> interleavedItems = new ArrayList<>();
        java.util.Set<String> seenLinks = new java.util.HashSet<>();

        int maxSize = Math.max(financeItems.size(), budgetItems.size());

        for (int i = 0; i < maxSize; i++) {
            // "재정" 뉴스에서 하나 추가
            if (i < financeItems.size()) {
                NewsDto.NaverNewsItem item = financeItems.get(i);
                if (!seenLinks.contains(item.getLink())) {
                    interleavedItems.add(item);
                    seenLinks.add(item.getLink());
                }
            }
            // "예산" 뉴스에서 하나 추가
            if (i < budgetItems.size()) {
                NewsDto.NaverNewsItem item = budgetItems.get(i);
                if (!seenLinks.contains(item.getLink())) {
                    interleavedItems.add(item);
                    seenLinks.add(item.getLink());
                }
            }
        }

        return interleavedItems.stream()
                .map(NewsDto.NaverNewsItem::toNewsResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}
