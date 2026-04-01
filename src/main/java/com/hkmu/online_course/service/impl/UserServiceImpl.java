package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.repository.IUserRepository;
import com.hkmu.online_course.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of IUserService.
 * Handles user registration, authentication support, and user management.
 */
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(IUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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
}
