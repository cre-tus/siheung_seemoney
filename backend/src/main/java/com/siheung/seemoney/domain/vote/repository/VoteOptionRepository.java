package com.siheung.seemoney.domain.vote.repository;

import com.siheung.seemoney.domain.vote.entity.Vote;
import com.siheung.seemoney.domain.vote.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteOptionRepository
        extends JpaRepository<VoteOption, Long> {

    List<VoteOption> findByVote(Vote vote);
}