package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Comment;
import com.hkmu.online_course.model.User;
import com.hkmu.online_course.repository.ICommentRepository;
import com.hkmu.online_course.repository.IUserRepository;
import com.hkmu.online_course.service.ICommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private ICommentRepository commentRepo;

    @Mock
    private IUserRepository userRepo;

    private ICommentService commentService;

    private User user;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl(commentRepo, userRepo);
        user = new User("alice", "Alice", "a@a.com", "12345678", "pass");
    }

    // --- findById ---

    @Test
    void findById_existingComment_returnsComment() {
        Comment comment = new Comment("comment-1", user, 1L, "LECTURE", "Great lecture!");
        when(commentRepo.findById("comment-1")).thenReturn(Optional.of(comment));

        Comment result = commentService.findById("comment-1");

        assertNotNull(result);
        assertEquals("Great lecture!", result.getContent());
    }

    @Test
    void findById_nonExistingComment_throwsException() {
        when(commentRepo.findById("ghost")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> commentService.findById("ghost"));

        assertTrue(ex.getMessage().contains("Comment not found"));
    }

    // --- findByTarget ---

    @Test
    void findByTarget_returnsCommentsForLecture() {
        Comment c1 = new Comment("c1", user, 1L, "LECTURE", "Comment 1");
        Comment c2 = new Comment("c2", user, 1L, "LECTURE", "Comment 2");
        when(commentRepo.findByTarget(1L, "LECTURE")).thenReturn(List.of(c1, c2));

        List<Comment> result = commentService.findByTarget(1L, "LECTURE");

        assertEquals(2, result.size());
    }

    @Test
    void findByTarget_returnsCommentsForPoll() {
        Comment c1 = new Comment("c1", user, 1L, "POLL", "Poll comment");
        when(commentRepo.findByTarget(1L, "POLL")).thenReturn(List.of(c1));

        List<Comment> result = commentService.findByTarget(1L, "POLL");

        assertEquals(1, result.size());
    }

    // --- findByUsername ---

    @Test
    void findByUsername_returnsUserComments() {
        Comment c1 = new Comment("c1", user, 1L, "LECTURE", "Alice's comment");
        when(commentRepo.findByUsername("alice")).thenReturn(List.of(c1));

        List<Comment> result = commentService.findByUsername("alice");

        assertEquals(1, result.size());
    }

    // --- create ---

    @Test
    void create_validLectureComment_savesComment() {
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(commentRepo.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        Comment result = commentService.create("alice", 1L, "LECTURE", "Excellent lecture!");

        assertNotNull(result);
        assertEquals("LECTURE", result.getTargetType());
        assertEquals(1L, result.getTargetId());
        assertEquals("Excellent lecture!", result.getContent());
        verify(commentRepo).save(any(Comment.class));
    }

    @Test
    void create_validPollComment_savesComment() {
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(commentRepo.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        Comment result = commentService.create("alice", 1L, "POLL", "Good poll!");

        assertNotNull(result);
        assertEquals("POLL", result.getTargetType());
        verify(commentRepo).save(any(Comment.class));
    }

    @Test
    void create_invalidTargetType_throwsException() {
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> commentService.create("alice", 1L, "INVALID", "Comment"));

        assertTrue(ex.getMessage().contains("Invalid target type"));
        verify(commentRepo, never()).save(any());
    }

    @Test
    void create_userNotFound_throwsException() {
        when(userRepo.findById("ghost")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> commentService.create("ghost", 1L, "LECTURE", "Comment"));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    // --- deleteById ---

    @Test
    void deleteById_existingComment_deletes() {
        when(commentRepo.existsById("comment-1")).thenReturn(true);
        doNothing().when(commentRepo).deleteById("comment-1");

        commentService.deleteById("comment-1");

        verify(commentRepo).deleteById("comment-1");
    }

    @Test
    void deleteById_nonExistingComment_throwsException() {
        when(commentRepo.existsById("ghost")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> commentService.deleteById("ghost"));

        assertTrue(ex.getMessage().contains("Comment not found"));
        verify(commentRepo, never()).deleteById(any());
    }
}
