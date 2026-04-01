package com.hkmu.online_course.service;

import com.hkmu.online_course.model.Poll;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Poll operations.
 */
public interface IPollService {

    Optional<Poll> findById(Long pollId);

    List<Poll> findAll();

    List<Poll> findOpenPolls();

    Poll create(String question, String option1, String option2, String option3,
               String option4, String option5, Long closeTime);

    void deleteById(Long pollId);
}
