package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Comment;
import com.hkmu.online_course.model.User;
import com.hkmu.online_course.service.ICommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private ICommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private UserDetails userDetails;

    @Test
    void create_lectureComment_redirectsToLecture() {
        when(userDetails.getUsername()).thenReturn("alice");
        when(commentService.create(eq("alice"), eq(1L), eq("LECTURE"), eq("Great lecture!"))).thenReturn(new Comment());

        String result = commentController.create(userDetails, 1L, "LECTURE", "Great lecture!");

        assertEquals("redirect:/lecture/1", result);
        verify(commentService).create("alice", 1L, "LECTURE", "Great lecture!");
    }

    @Test
    void create_pollComment_redirectsToPoll() {
        when(userDetails.getUsername()).thenReturn("alice");
        when(commentService.create(eq("alice"), eq(2L), eq("POLL"), eq("Nice poll!"))).thenReturn(new Comment());

        String result = commentController.create(userDetails, 2L, "POLL", "Nice poll!");

        assertEquals("redirect:/poll/2", result);
        verify(commentService).create("alice", 2L, "POLL", "Nice poll!");
    }

    @Test
    void delete_byOwner_redirectsToLecture() {
        User owner = new User("alice", "Alice", "a@a.com", "12345678", "pass");
        Comment comment = new Comment("c1", owner, 1L, "LECTURE", "My comment");
        when(commentService.findById("c1")).thenReturn(comment);
        doNothing().when(commentService).deleteById("c1");
        when(userDetails.getUsername()).thenReturn("alice");

        String result = commentController.delete("c1", userDetails);

        assertEquals("redirect:/lecture/1", result);
        verify(commentService).deleteById("c1");
    }

    @Test
    void delete_notOwner_doesNotDelete() {
        User owner = new User("bob", "Bob", "b@b.com", "22222222", "pass");
        Comment comment = new Comment("c1", owner, 1L, "LECTURE", "Bob's comment");
        when(commentService.findById("c1")).thenReturn(comment);
        when(userDetails.getUsername()).thenReturn("alice");

        String result = commentController.delete("c1", userDetails);

        assertEquals("redirect:/lecture/1", result);
        verify(commentService, never()).deleteById(any());
    }

    @Test
    void delete_pollComment_redirectsToPoll() {
        User owner = new User("alice", "Alice", "a@a.com", "12345678", "pass");
        Comment comment = new Comment("c1", owner, 2L, "POLL", "Poll comment");
        when(commentService.findById("c1")).thenReturn(comment);
        doNothing().when(commentService).deleteById("c1");
        when(userDetails.getUsername()).thenReturn("alice");

        String result = commentController.delete("c1", userDetails);

        assertEquals("redirect:/poll/2", result);
        verify(commentService).deleteById("c1");
    }
}
