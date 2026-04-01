package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Internal Spring Data JPA repository for Lecture entity.
 * HIDDEN from Service layer - only used internally by LectureRepositoryImpl.
 */
public interface LectureJpaSpringRepo extends JpaRepository<Lecture, Long> {
}
