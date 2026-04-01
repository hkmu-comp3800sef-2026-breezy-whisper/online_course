package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Internal Spring Data JPA repository for User entity.
 * HIDDEN from Service layer - only used internally by UserRepositoryImpl.
 * Exists to satisfy DIP - Service only sees IUserRepository.
 */
public interface UserJpaSpringRepo extends JpaRepository<User, String> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
