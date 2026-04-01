package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Internal Spring Data JPA repository for Poll entity.
 * HIDDEN from Service layer - only used internally by PollRepositoryImpl.
 */
public interface PollJpaSpringRepo extends JpaRepository<Poll, Long> {
}
