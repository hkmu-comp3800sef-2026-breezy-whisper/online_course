package com.hkmu.online_course.repository;

import com.hkmu.online_course.model.Poll;
import java.util.List;
import java.util.Optional;

/**
 * Clean repository interface for Poll entity.
 * Service layer depends ONLY on this interface (DIP).
 */
public interface IPollRepository {

    Optional<Poll> findById(Long pollId);

    List<Poll> findAll();

    long count();

    boolean existsById(Long pollId);

    Poll save(Poll poll);

    void deleteById(Long pollId);

    // QueryDSL methods
    List<Poll> findOpenPolls();
}
