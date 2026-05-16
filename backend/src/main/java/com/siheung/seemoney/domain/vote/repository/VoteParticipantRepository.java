package com.siheung.seemoney.domain.vote.repository;

import com.siheung.seemoney.domain.user.entity.User;
import com.siheung.seemoney.domain.vote.entity.Vote;
import com.siheung.seemoney.domain.vote.entity.VoteParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteParticipantRepository
        extends JpaRepository<VoteParticipant, Long> {

    boolean existsByUserAndVote(
            User user,
            Vote vote
    );

    List<VoteParticipant> findByVote(
            Vote vote
    );
}