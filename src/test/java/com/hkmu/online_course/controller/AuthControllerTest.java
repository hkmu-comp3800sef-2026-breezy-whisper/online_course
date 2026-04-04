package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_returnsLoginView() {
        String viewName = authController.login();
        assertEquals("login", viewName);
    }

    @Test
    void register_returnsRegisterView() {
        String viewName = authController.register();
        assertEquals("register", viewName);
    }

    @Test
    void registerSubmit_validStudent_redirectsToLoginWithRegistered() {
        when(userService.registerStudent(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new User("alice", "Alice Chan", "alice@example.com", "12345678", "pass"));

        String result = authController.registerSubmit("alice", "password123", "Alice Chan", "alice@example.com", "12345678", "student");

        assertEquals("redirect:/login?registered", result);
        verify(userService).registerStudent("alice", "password123", "Alice Chan", "alice@example.com", "12345678");
    }

    @Test
    void registerSubmit_validTeacher_redirectsToLoginWithRegistered() {
        when(userService.registerTeacher(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new User("teacher1", "Teacher One", "teacher@example.com", "12345678", "pass"));

        String result = authController.registerSubmit("teacher1", "password123", "Teacher One", "teacher@example.com", "12345678", "teacher");

        assertEquals("redirect:/login?registered", result);
        verify(userService).registerTeacher("teacher1", "password123", "Teacher One", "teacher@example.com", "12345678");
    }

    @Test
    void registerSubmit_duplicateUsername_redirectsWithError() {
        when(userService.registerStudent(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        String result = authController.registerSubmit("alice", "password123", "Alice Chan", "alice@example.com", "12345678", "student");

        assertEquals("redirect:/register?error=Username already exists", result);
    }
}
