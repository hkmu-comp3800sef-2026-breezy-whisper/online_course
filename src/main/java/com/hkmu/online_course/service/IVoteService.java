package com.hkmu.online_course.service;

import com.hkmu.online_course.model.Vote;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Vote operations.
 */
public interface IVoteService {

    Optional<Vote> findById(String voteId);

    List<Vote> findByPollId(Long pollId);

    Optional<Vote> findByPollIdAndUsername(Long pollId, String username);

    void vote(String username, Long pollId, Integer selectedOption);

    void deleteByUsername(String username);
}
