package com.hkmu.online_course.repository;

import com.hkmu.online_course.model.Vote;
import java.util.List;
import java.util.Optional;

/**
 * Clean repository interface for Vote entity.
 * Service layer depends ONLY on this interface (DIP).
 */
public interface IVoteRepository {

    Optional<Vote> findById(String voteId);

    List<Vote> findAll();

    long count();

    boolean existsById(String voteId);

    Vote save(Vote vote);

    void deleteById(String voteId);

    // QueryDSL methods
    List<Vote> findByPollId(Long pollId);

    long countByPollId(Long pollId);

    Optional<Vote> findByPollIdAndUsername(Long pollId, String username);

    boolean existsByPollIdAndUsername(Long pollId, String username);

    void deleteByPollId(Long pollId);
}
