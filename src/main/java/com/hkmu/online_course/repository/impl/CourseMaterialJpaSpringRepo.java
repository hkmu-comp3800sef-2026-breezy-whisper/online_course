package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Internal Spring Data JPA repository for CourseMaterial entity.
 * HIDDEN from Service layer - only used internally by CourseMaterialRepositoryImpl.
 */
public interface CourseMaterialJpaSpringRepo extends JpaRepository<CourseMaterial, Long> {
}
