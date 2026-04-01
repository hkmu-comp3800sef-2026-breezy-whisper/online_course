package com.hkmu.online_course.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * User entity representing registered students and teachers.
 * Username serves as the natural primary key with strict format constraints.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @Size(max = 32, message = "Username must not exceed 32 characters")
    @Pattern(regexp = "^[A-Za-z][0-9A-Za-z_-]{0,31}$",
            message = "Username must start with a letter and contain only letters, digits, hyphens, and underscores")
    @Column(name = "username", length = 32, nullable = false, unique = true)
    private String username;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 8)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String password;

    /**
     * User role: 0 = student (ROLE_STUDENT), 1 = teacher (ROLE_TEACHER).
     */
    @Column(nullable = false)
    private Integer role = 0;

    /**
     * Account status: 0 = active, 1 = pending (teacher awaiting approval), 2 = disabled.
     */
    @Column(nullable = false)
    private Integer status = 0;

    @Column(name = "disabled_reason", length = 500)
    private String disabledReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public User() {
    }

    public User(String username, String fullName, String email, String phoneNumber, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = 0; // Default to student
        this.status = 0; // Default to active
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDisabledReason() {
        return disabledReason;
    }

    public void setDisabledReason(String disabledReason) {
        this.disabledReason = disabledReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Helper methods for role and status checks
    public boolean isTeacher() {
        return role != null && role == 1;
    }

    public boolean isStudent() {
        return role != null && role == 0;
    }

    public boolean isActive() {
        return status != null && status == 0;
    }

    public boolean isPending() {
        return status != null && status == 1;
    }

    public boolean isDisabled() {
        return status != null && status == 2;
    }
}
