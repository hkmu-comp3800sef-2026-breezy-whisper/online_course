package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.model.Vote;
import com.hkmu.online_course.service.ICommentService;
import com.hkmu.online_course.service.IPollService;
import com.hkmu.online_course.service.IVoteService;
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
class PollControllerTest {

    @Mock
    private IPollService pollService;

    @Mock
    private IVoteService voteService;

    @Mock
    private ICommentService commentService;

    @InjectMocks
    private PollController pollController;

    @Mock
    private Model model;

    @Mock
    private UserDetails userDetails;

    @Test
    void list_returnsPollListView() {
        Poll poll = new Poll("Best language?", "Java", "Python", "Go", "Rust", "C++", -1L);
        poll.setPollId(1L);
        when(pollService.findAll()).thenReturn(List.of(poll));

        String viewName = pollController.list(model);

        assertEquals("poll/list", viewName);
        verify(model).addAttribute("polls", List.of(poll));
    }

    @Test
    void view_existingPoll_returnsPollView() {
        Poll poll = new Poll("Best language?", "Java", "Python", "Go", "Rust", "C++", -1L);
        poll.setPollId(1L);
        when(pollService.findById(1L)).thenReturn(Optional.of(poll));
        when(voteService.findByPollId(1L)).thenReturn(List.of());
        when(commentService.findByTarget(1L, "POLL")).thenReturn(List.of());

        String viewName = pollController.view(1L, null, model);

        assertEquals("poll/view", viewName);
        verify(model).addAttribute("poll", poll);
        verify(model).addAttribute("voteCounts", java.util.Collections.emptyMap());
        verify(model).addAttribute("userVote", null);
        verify(model).addAttribute("comments", List.of());
    }

    @Test
    void createForm_returnsCreateView() {
        String viewName = pollController.createForm();
        assertEquals("poll/create", viewName);
    }

    @Test
    void createSubmit_validPoll_redirectsToList() {
        when(pollService.create(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(new Poll("Which topic?", "Java", "Python", "Go", "Rust", "C++", -1L));

        String result = pollController.create("Which topic?", "Java", "Python", "Go", "Rust", "C++", null);

        assertEquals("redirect:/poll/list", result);
        verify(pollService).create("Which topic?", "Java", "Python", "Go", "Rust", "C++", -1L);
    }

    @Test
    void delete_existingPoll_redirectsToList() {
        doNothing().when(pollService).deleteById(1L);

        String result = pollController.delete(1L);

        assertEquals("redirect:/poll/list", result);
        verify(pollService).deleteById(1L);
    }
}
