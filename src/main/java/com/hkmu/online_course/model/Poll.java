package com.hkmu.online_course.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Poll entity representing a multiple-choice poll question.
 * Contains exactly 5 options and an optional close time for voting.
 */
@Entity
@Table(name = "poll")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long pollId;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(name = "option_1", nullable = false, length = 255)
    private String option1;

    @Column(name = "option_2", nullable = false, length = 255)
    private String option2;

    @Column(name = "option_3", nullable = false, length = 255)
    private String option3;

    @Column(name = "option_4", nullable = false, length = 255)
    private String option4;

    @Column(name = "option_5", nullable = false, length = 255)
    private String option5;

    /**
     * Close time for voting as epoch milliseconds.
     * -1 means the poll never closes.
     */
    @Column(name = "close_time", nullable = false)
    private Long closeTime = -1L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Transient field for vote count display.
     * Not persisted - populated by service layer when fetching polls.
     */
    @Transient
    private Long voteCount = 0L;

    // Constructors
    public Poll() {
    }

    public Poll(String question, String option1, String option2, String option3,
               String option4, String option5, Long closeTime) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
        this.closeTime = closeTime != null ? closeTime : -1L;
    }

    // Getters and Setters
    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Check if the poll is still open for voting.
     * @return true if voting is allowed, false if poll is closed
     */
    public boolean isOpen() {
        if (closeTime == null || closeTime == -1L) {
            return true;
        }
        return System.currentTimeMillis() < closeTime;
    }

    /**
     * Check if the poll is closed for voting.
     * @return true if voting is closed, false if poll is still open
     */
    public boolean isClosed() {
        return !isOpen();
    }

    /**
     * Get the total vote count for this poll.
     * @return number of votes
     */
    public Long getVoteCount() {
        return voteCount;
    }

    /**
     * Set the total vote count for this poll.
     * @param voteCount number of votes
     */
    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }
}
