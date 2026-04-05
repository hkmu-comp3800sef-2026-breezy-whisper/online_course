package com.hkmu.online_course.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Comment entity representing user comments on lectures or polls.
 * Target type determines whether the comment is on a LECTURE or POLL.
 */
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @Column(name = "comment_id", length = 36)
    private String commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    /**
     * ID of the target entity (lecture_id or poll_id).
     */
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    /**
     * Type of target entity: "LECTURE" or "POLL".
     */
    @Column(name = "target_type", nullable = false, length = 20)
    private String targetType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Comment() {
    }

    public Comment(String commentId, User user, Long targetId, String targetType, String content) {
        this.commentId = commentId;
        this.user = user;
        this.targetId = targetId;
        this.targetType = targetType;
        this.content = content;
    }

    // Getters and Setters
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Constants for target types
    public static final String TARGET_TYPE_LECTURE = "LECTURE";
    public static final String TARGET_TYPE_POLL = "POLL";

    /**
     * Check if this comment is on a lecture.
     * @return true if target type is LECTURE
     */
    public boolean isOnLecture() {
        return TARGET_TYPE_LECTURE.equals(targetType);
    }

    /**
     * Check if this comment is on a poll.
     * @return true if target type is POLL
     */
    public boolean isOnPoll() {
        return TARGET_TYPE_POLL.equals(targetType);
    }
}
