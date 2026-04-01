package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Internal Spring Data JPA repository for Comment entity.
 * HIDDEN from Service layer - only used internally by CommentRepositoryImpl.
 */
public interface CommentJpaSpringRepo extends JpaRepository<Comment, String> {
}
