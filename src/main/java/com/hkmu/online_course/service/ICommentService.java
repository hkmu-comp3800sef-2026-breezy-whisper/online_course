package com.hkmu.online_course.service;

import com.hkmu.online_course.model.Comment;
import java.util.List;

/**
 * Service interface for Comment operations.
 */
public interface ICommentService {

    Comment findById(String commentId);

    List<Comment> findByTarget(Long targetId, String targetType);

    List<Comment> findByUsername(String username);

    Comment create(String username, Long targetId, String targetType, String content);

    void deleteById(String commentId);
}
