package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Comment;
import com.hkmu.online_course.model.User;
import com.hkmu.online_course.service.ICommentService;
import com.hkmu.online_course.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private ICommentService commentService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Model model;

    @Mock
    private UserDetails userDetails;

    @Test
    void profile_returnsProfileView() {
        User user = new User("alice", "Alice Chan", "alice@example.com", "12345678", "encodedPass");
        when(userService.findById("alice")).thenReturn(Optional.of(user));
        when(userDetails.getUsername()).thenReturn("alice");

        String viewName = userController.profile(userDetails, model);

        assertEquals("user/profile", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    void updateProfile_validData_redirectsToProfileWithUpdated() {
        when(userService.updateProfile(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new User("alice", "Alice Updated", "alice@example.com", "87654321", "pass"));
        when(userDetails.getUsername()).thenReturn("alice");

        String result = userController.updateProfile(userDetails, "Alice Updated", "alice@example.com", "87654321");

        assertEquals("redirect:/user/profile?updated", result);
        verify(userService).updateProfile("alice", "Alice Updated", "alice@example.com", "87654321");
    }

    @Test
    void comments_returnsCommentsView() {
        User user = new User("alice", "Alice", "a@a.com", "12345678", "pass");
        Comment comment = new Comment("c1", user, 1L, "LECTURE", "My comment");
        when(commentService.findByUsername("alice")).thenReturn(List.of(comment));
        when(userDetails.getUsername()).thenReturn("alice");

        String viewName = userController.comments(userDetails, model);

        assertEquals("user/comments", viewName);
        verify(model).addAttribute("comments", List.of(comment));
    }
}
