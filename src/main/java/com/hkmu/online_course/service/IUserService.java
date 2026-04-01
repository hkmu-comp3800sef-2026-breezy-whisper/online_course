package com.hkmu.online_course.service;

import com.hkmu.online_course.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for User operations.
 * Follows DIP - callers depend on this interface, not implementation.
 */
public interface IUserService {

    Optional<User> findById(String username);

    List<User> findAll();

    List<User> findByRole(Integer role);

    List<User> findByStatus(Integer status);

    User registerStudent(String username, String password, String fullName,
                        String email, String phoneNumber);

    User registerTeacher(String username, String password, String fullName,
                       String email, String phoneNumber);

    void activateTeacher(String username);

    void disableUser(String username, String reason);

    void enableUser(String username);

    User updateProfile(String username, String fullName, String email, String phoneNumber);
}
