package com.hkmu.online_course.repository;

import com.hkmu.online_course.model.Comment;
import java.util.List;
import java.util.Optional;

/**
 * Clean repository interface for Comment entity.
 * Service layer depends ONLY on this interface (DIP).
 */
public interface ICommentRepository {

    Optional<Comment> findById(String commentId);

    List<Comment> findAll();

    long count();

    boolean existsById(String commentId);

    Comment save(Comment comment);

    void deleteById(String commentId);

    // QueryDSL methods
    List<Comment> findByTarget(Long targetId, String targetType);

    List<Comment> findByUsername(String username);
}
