package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private Model model;

    @Test
    void listUsers_returnsAdminUsersView() {
        User user1 = new User("alice", "Alice", "a@a.com", "11111111", "pass");
        User user2 = new User("teacher1", "Teacher", "t@t.com", "22222222", "pass");
        user2.setRole(1);
        when(userService.findAll()).thenReturn(List.of(user1, user2));

        String viewName = adminController.listUsers(model);

        assertEquals("admin/users", viewName);
        verify(model).addAttribute("users", List.of(user1, user2));
    }

    @Test
    void activateTeacher_validTeacher_redirectsToUsers() {
        doNothing().when(userService).activateTeacher("teacher2");

        String result = adminController.activateTeacher("teacher2");

        assertEquals("redirect:/admin/users", result);
        verify(userService).activateTeacher("teacher2");
    }

    @Test
    void disableUser_withReason_redirectsToUsers() {
        doNothing().when(userService).disableUser("alice", "Inappropriate behaviour");

        String result = adminController.disableUser("alice", "Inappropriate behaviour");

        assertEquals("redirect:/admin/users", result);
        verify(userService).disableUser("alice", "Inappropriate behaviour");
    }

    @Test
    void enableUser_disabledUser_redirectsToUsers() {
        doNothing().when(userService).enableUser("alice");

        String result = adminController.enableUser("alice");

        assertEquals("redirect:/admin/users", result);
        verify(userService).enableUser("alice");
    }
}
