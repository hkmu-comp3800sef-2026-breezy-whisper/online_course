package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.repository.IUserRepository;
import com.hkmu.online_course.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import com.hkmu.online_course.service.ICommentService;
import com.hkmu.online_course.service.IVoteService;
import com.hkmu.online_course.service.ILectureService;
import com.hkmu.online_course.service.ICourseMaterialService;
import com.hkmu.online_course.repository.ICommentRepository;
import com.hkmu.online_course.repository.IVoteRepository;

/**
 * Implementation of IUserService.
 * Handles user registration, authentication support, and user management.
 */
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ICommentService commentService;
    private final IVoteService voteService;
    private final ILectureService lectureService;
    private final ICourseMaterialService courseMaterialService;
    private final ICommentRepository commentRepo;
    private final IVoteRepository voteRepo;

    @Autowired
    public UserServiceImpl(IUserRepository userRepo, PasswordEncoder passwordEncoder,
                          ICommentService commentService, IVoteService voteService,
                          ILectureService lectureService, ICourseMaterialService courseMaterialService,
                          ICommentRepository commentRepo, IVoteRepository voteRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.commentService = commentService;
        this.voteService = voteService;
        this.lectureService = lectureService;
        this.courseMaterialService = courseMaterialService;
        this.commentRepo = commentRepo;
        this.voteRepo = voteRepo;
    }

    @Override
    public Optional<User> findById(String username) {
        return userRepo.findById(username);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public List<User> findByRole(Integer role) {
        return userRepo.findByRole(role);
    }

    @Override
    public List<User> findByStatus(Integer status) {
        return userRepo.findByStatus(status);
    }

    @Override
    public User registerStudent(String username, String password, String fullName,
                                String email, String phoneNumber) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(username, fullName, email, phoneNumber,
                passwordEncoder.encode(password));
        user.setRole(0); // Student
        user.setStatus(0); // Active

        return userRepo.save(user);
    }

    @Override
    public User registerTeacher(String username, String password, String fullName,
                                String email, String phoneNumber) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(username, fullName, email, phoneNumber,
                passwordEncoder.encode(password));
        user.setRole(1); // Teacher
        user.setStatus(1); // Pending approval

        return userRepo.save(user);
    }

    @Override
    public void activateTeacher(String username) {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (!user.isTeacher()) {
            throw new IllegalArgumentException("User is not a teacher");
        }
        if (!user.isPending()) {
            throw new IllegalArgumentException("Teacher is not pending activation");
        }

        user.setStatus(0); // Active
        userRepo.save(user);
    }

    @Override
    public void disableUser(String username, String reason) {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        user.setStatus(2); // Disabled
        user.setDisabledReason(reason);
        userRepo.save(user);
    }

    @Override
    public void enableUser(String username) {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        user.setStatus(0); // Active
        user.setDisabledReason(null);
        userRepo.save(user);
    }

    @Override
    public User updateProfile(String username, String fullName, String email, String phoneNumber) {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Check email uniqueness if changing
        if (!user.getEmail().equals(email) && userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        return userRepo.save(user);
    }

    @Override
    @Transactional
    public void deleteById(String username, String currentUsername) {
        if (username.equals(currentUsername)) {
            throw new IllegalArgumentException("Cannot delete your own account");
        }

        User user = userRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Delete user's comments
        commentService.findByUsername(username).forEach(c -> commentService.deleteById(c.getCommentId()));

        // Delete user's votes
        voteService.deleteByUsername(username);

        userRepo.deleteById(username);
    }

    @Override
    @Transactional
    public void changeUsername(String oldUsername, String newUsername) {
        if (oldUsername.equals(newUsername)) {
            throw new IllegalArgumentException("New username must be different");
        }

        // Validate new username format (same as @Pattern)
        if (!newUsername.matches("^[A-Za-z][0-9A-Za-z_-]{0,31}$")) {
            throw new IllegalArgumentException("Invalid username format");
        }

        User oldUser = userRepo.findById(oldUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + oldUsername));

        if (userRepo.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("New username already exists");
        }

        // Create new user copying all fields
        User newUser = new User(newUsername, oldUser.getFullName(), oldUser.getEmail(), 
                                oldUser.getPhoneNumber(), oldUser.getPassword());
        newUser.setRole(oldUser.getRole());
        newUser.setStatus(oldUser.getStatus());
        newUser.setDisabledReason(oldUser.getDisabledReason());
        // Note: timestamps managed by Hibernate

        userRepo.save(newUser);

        // Transfer comments
        commentService.findByUsername(oldUsername).stream()
            .peek(comment -> comment.setUser(newUser))
            .forEach(commentRepo::save);

        // Transfer votes
        voteService.findByUsername(oldUsername).stream()
            .peek(vote -> vote.setUser(newUser))
            .forEach(voteRepo::save);

        // Delete old user
        userRepo.deleteById(oldUsername);
    }
}



