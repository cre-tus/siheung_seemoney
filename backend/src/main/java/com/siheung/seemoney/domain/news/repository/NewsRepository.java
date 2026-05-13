package com.siheung.seemoney.domain.news.repository;

import com.siheung.seemoney.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsByLink(String link);
}
