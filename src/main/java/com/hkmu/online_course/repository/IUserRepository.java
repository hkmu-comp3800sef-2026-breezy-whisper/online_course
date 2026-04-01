package com.hkmu.online_course.repository;

import com.hkmu.online_course.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Clean repository interface for User entity.
 * Service layer depends ONLY on this interface (DIP).
 * Implementation details (JpaRepository, QueryDSL, database) are hidden.
 */
public interface IUserRepository {

    Optional<User> findById(String username);

    List<User> findAll();

    long count();

    boolean existsById(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User save(User user);

    void deleteById(String username);

    // QueryDSL methods
    List<User> findByRole(Integer role);

    List<User> findByStatus(Integer status);
}
