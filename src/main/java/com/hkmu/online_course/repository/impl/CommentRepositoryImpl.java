package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.QComment;
import com.hkmu.online_course.model.QUser;
import com.hkmu.online_course.model.Comment;
import com.hkmu.online_course.repository.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL-based implementation of ICommentRepository.
 * Delegates CRUD to CommentJpaSpringRepo, uses JPAQueryFactory for complex queries.
 */
@Repository
public class CommentRepositoryImpl extends AbstractRepositoryImpl implements ICommentRepository {

    private final CommentJpaSpringRepo springRepo;

    @Autowired
    public CommentRepositoryImpl(CommentJpaSpringRepo springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Comment> findById(String commentId) {
        return springRepo.findById(commentId);
    }

    @Override
    public List<Comment> findAll() {
        return springRepo.findAll();
    }

    @Override
    public long count() {
        return springRepo.count();
    }

    @Override
    public boolean existsById(String commentId) {
        return springRepo.existsById(commentId);
    }

    @Override
    public Comment save(Comment comment) {
        return springRepo.save(comment);
    }

    @Override
    public void deleteById(String commentId) {
        springRepo.deleteById(commentId);
    }

    // QueryDSL methods
    @Override
    public List<Comment> findByTarget(Long targetId, String targetType) {
        QComment comment = QComment.comment;
        return queryFactory
                .selectFrom(comment)
                .where(comment.targetId.eq(targetId)
                        .and(comment.targetType.eq(targetType)))
                .fetch();
    }

    @Override
    public List<Comment> findByUsername(String username) {
        QComment comment = QComment.comment;
        return queryFactory
                .selectFrom(comment)
                .join(comment.user, QUser.user)
                .where(QUser.user.username.eq(username))
                .fetch();
    }

    @Override
    public void deleteByTarget(Long targetId, String targetType) {
        QComment comment = QComment.comment;
        queryFactory
                .delete(comment)
                .where(comment.targetId.eq(targetId)
                        .and(comment.targetType.eq(targetType)))
                .execute();
    }
}
