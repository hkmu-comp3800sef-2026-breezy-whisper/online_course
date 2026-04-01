package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Internal Spring Data JPA repository for Vote entity.
 * HIDDEN from Service layer - only used internally by VoteRepositoryImpl.
 */
public interface VoteJpaSpringRepo extends JpaRepository<Vote, String> {
}
