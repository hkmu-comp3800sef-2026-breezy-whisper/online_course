package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.repository.IPollRepository;
import com.hkmu.online_course.repository.IVoteRepository;
import com.hkmu.online_course.service.IPollService;
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
class PollServiceImplTest {

    @Mock
    private IPollRepository pollRepo;

    @Mock
    private IVoteRepository voteRepo;

    private IPollService pollService;

    @BeforeEach
    void setUp() {
        pollService = new PollServiceImpl(pollRepo, voteRepo);
    }

    // --- findById ---

    @Test
    void findById_existingPoll_returnsPollWithVoteCount() {
        Poll poll = new Poll("What is 2+2?", "1", "2", "3", "4", "5", -1L);
        poll.setPollId(1L);
        when(pollRepo.findById(1L)).thenReturn(Optional.of(poll));
        when(voteRepo.countByPollId(1L)).thenReturn(5L);

        Optional<Poll> result = pollService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getVoteCount());
    }

    @Test
    void findById_nonExistingPoll_returnsEmpty() {
        when(pollRepo.findById(999L)).thenReturn(Optional.empty());

        Optional<Poll> result = pollService.findById(999L);

        assertFalse(result.isPresent());
    }

    // --- findAll ---

    @Test
    void findAll_returnsAllPollsWithVoteCounts() {
        Poll p1 = new Poll("Q1", "1", "2", "3", "4", "5", -1L);
        p1.setPollId(1L);
        Poll p2 = new Poll("Q2", "a", "b", "c", "d", "e", -1L);
        p2.setPollId(2L);
        when(pollRepo.findAll()).thenReturn(List.of(p1, p2));
        when(voteRepo.countByPollId(1L)).thenReturn(3L);
        when(voteRepo.countByPollId(2L)).thenReturn(7L);

        List<Poll> result = pollService.findAll();

        assertEquals(2, result.size());
        assertEquals(3L, result.get(0).getVoteCount());
        assertEquals(7L, result.get(1).getVoteCount());
    }

    // --- findOpenPolls ---

    @Test
    void findOpenPolls_returnsOpenPollsWithVoteCounts() {
        Poll poll = new Poll("Open Poll?", "yes", "no", "maybe", "idk", "pass", -1L);
        poll.setPollId(1L);
        when(pollRepo.findOpenPolls()).thenReturn(List.of(poll));
        when(voteRepo.countByPollId(1L)).thenReturn(10L);

        List<Poll> result = pollService.findOpenPolls();

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getVoteCount());
    }

    // --- create ---

    @Test
    void create_validPoll_savesAndReturns() {
        Poll savedPoll = new Poll("Which topic next?", "Java", "Python", "Go", "Rust", "C++", -1L);
        savedPoll.setPollId(1L);
        when(pollRepo.save(any(Poll.class))).thenReturn(savedPoll);

        Poll result = pollService.create("Which topic next?", "Java", "Python", "Go", "Rust", "C++", -1L);

        assertNotNull(result);
        assertEquals("Which topic next?", result.getQuestion());
        verify(pollRepo).save(any(Poll.class));
    }

    @Test
    void create_withCloseTime_savesWithCloseTime() {
        Poll savedPoll = new Poll("Time-limited poll", "A", "B", "C", "D", "E", 9999999999L);
        savedPoll.setPollId(1L);
        when(pollRepo.save(any(Poll.class))).thenReturn(savedPoll);

        Poll result = pollService.create("Time-limited poll", "A", "B", "C", "D", "E", 9999999999L);

        assertEquals(9999999999L, result.getCloseTime());
    }

    // --- deleteById ---

    @Test
    void deleteById_existingPoll_deletes() {
        when(pollRepo.existsById(1L)).thenReturn(true);
        doNothing().when(pollRepo).deleteById(1L);

        pollService.deleteById(1L);

        verify(pollRepo).deleteById(1L);
    }

    @Test
    void deleteById_nonExistingPoll_throwsException() {
        when(pollRepo.existsById(999L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> pollService.deleteById(999L));

        assertTrue(ex.getMessage().contains("Poll not found"));
        verify(pollRepo, never()).deleteById(any());
    }
}
