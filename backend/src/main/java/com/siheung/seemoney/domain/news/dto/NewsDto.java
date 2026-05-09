package com.siheung.seemoney.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 네이버 뉴스 API 관련 DTO 클래스
 */
public class NewsDto {

    /**
     * 네이버 뉴스 검색 API의 응답을 매핑하는 클래스
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NaverNewsResponse {
        private String lastBuildDate;
        private int total;
        private int start;
        private int display;
        private List<NaverNewsItem> items;
    }

    /**
     * 네이버 뉴스 검색 결과의 개별 아이템
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NaverNewsItem {
        private String title;
        private String originallink;
        private String link;
        private String description;
        private String pubDate;

        /**
         * HTML 태그(<b>, </b>)를 제거한 클라이언트용 DTO로 변환
         */
        public NewsResponse toNewsResponse() {
            return NewsResponse.builder()
                    .title(cleanHtmlTags(this.title))
                    .link(this.link)
                    .summary(cleanHtmlTags(this.description))
                    .pubDate(this.pubDate)
                    .build();
        }

        private String cleanHtmlTags(String text) {
            if (text == null) return null;
            return text.replaceAll("<[^>]*>", "");
        }
    }

    /**
     * 안드로이드 클라이언트로 내려줄 커스텀 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsResponse {
        private String title;    // 기사 제목
        private String link;     // 뉴스 링크
        private String summary;  // 요약 내용
        private String pubDate;  // 발행일
    }

    /**
     * NaverNewsResponse를 List<NewsResponse>로 변환하는 유틸리티 메서드
     */
    public static List<NewsResponse> of(NaverNewsResponse response) {
        return response.getItems().stream()
                .map(NaverNewsItem::toNewsResponse)
                .collect(Collectors.toList());
    }
}
