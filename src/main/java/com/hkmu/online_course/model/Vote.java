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
import jakarta.persistence.UniqueConstraint;

/**
 * Vote entity representing a user's vote on a poll.
 * Each user can only have one vote per poll, enforced by unique constraint.
 */
@Entity
@Table(name = "vote", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"poll_id", "username"})
})
public class Vote {

    @Id
    @Column(name = "vote_id", length = 36)
    private String voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    /**
     * Selected option (1-5) for this vote.
     */
    @Column(name = "selected_option", nullable = false)
    private Integer selectedOption;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Vote() {
    }

    public Vote(String voteId, Poll poll, User user, Integer selectedOption) {
        this.voteId = voteId;
        this.poll = poll;
        this.user = user;
        this.selectedOption = selectedOption;
    }

    // Getters and Setters
    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Integer selectedOption) {
        this.selectedOption = selectedOption;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
