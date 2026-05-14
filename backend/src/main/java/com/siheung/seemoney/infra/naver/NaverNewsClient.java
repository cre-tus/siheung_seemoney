package com.siheung.seemoney.infra.naver;

import com.siheung.seemoney.domain.news.dto.NewsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 네이버 뉴스 검색 API 연동 클라이언트
 */
@Slf4j
@Component
public class NaverNewsClient {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 네이버 뉴스 검색 API 호출
     * @param query 검색어
     * @return NaverNewsResponse
     */
    public NewsDto.NaverNewsResponse searchNews(String query) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", query)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "date")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<NewsDto.NaverNewsResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    NewsDto.NaverNewsResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Naver News API 호출 실패: {}", e.getMessage());
            // 호출 실패 시 빈 결과 반환 또는 예외 전파 (여기서는 빈 리스트를 가진 객체 반환 시뮬레이션)
            return new NewsDto.NaverNewsResponse();
        }
    }
}
