package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Comment;
import com.hkmu.online_course.model.User;
import com.hkmu.online_course.repository.ICommentRepository;
import com.hkmu.online_course.repository.IUserRepository;
import com.hkmu.online_course.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of ICommentService.
 */
@Service
public class CommentServiceImpl implements ICommentService {

    private final ICommentRepository commentRepo;
    private final IUserRepository userRepo;

    @Autowired
    public CommentServiceImpl(ICommentRepository commentRepo, IUserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Comment findById(String commentId) {
        return commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));
    }

    @Override
    public List<Comment> findByTarget(Long targetId, String targetType) {
        return commentRepo.findByTarget(targetId, targetType);
    }

    @Override
    public List<Comment> findByUsername(String username) {
        return commentRepo.findByUsername(username);
    }

    @Override
    public Comment create(String username, Long targetId, String targetType, String content) {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Validate targetType
        if (!Comment.TARGET_TYPE_LECTURE.equals(targetType) &&
            !Comment.TARGET_TYPE_POLL.equals(targetType)) {
            throw new IllegalArgumentException("Invalid target type: " + targetType);
        }

        Comment comment = new Comment(
                UUID.randomUUID().toString(),
                user,
                targetId,
                targetType,
                content
        );

        return commentRepo.save(comment);
    }

    @Override
    public void deleteById(String commentId) {
        if (!commentRepo.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found: " + commentId);
        }
        commentRepo.deleteById(commentId);
    }

    @Override
    public void deleteByTarget(Long targetId, String targetType) {
        commentRepo.deleteByTarget(targetId, targetType);
    }
}
