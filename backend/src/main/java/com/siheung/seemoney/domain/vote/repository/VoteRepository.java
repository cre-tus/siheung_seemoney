package com.siheung.seemoney.domain.vote.repository;

import com.siheung.seemoney.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository
        extends JpaRepository<Vote, Long> {
}