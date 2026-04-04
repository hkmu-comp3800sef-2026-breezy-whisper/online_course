package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.model.User;
import com.hkmu.online_course.model.Vote;
import com.hkmu.online_course.repository.IPollRepository;
import com.hkmu.online_course.repository.IUserRepository;
import com.hkmu.online_course.repository.IVoteRepository;
import com.hkmu.online_course.service.IVoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceImplTest {

    @Mock
    private IVoteRepository voteRepo;

    @Mock
    private IPollRepository pollRepo;

    @Mock
    private IUserRepository userRepo;

    private IVoteService voteService;

    private Poll openPoll;
    private Poll closedPoll;
    private User user;

    @BeforeEach
    void setUp() {
        voteService = new VoteServiceImpl(voteRepo, pollRepo, userRepo);

        openPoll = new Poll("Open question?", "A", "B", "C", "D", "E", -1L);
        openPoll.setPollId(1L);

        closedPoll = new Poll("Closed question?", "A", "B", "C", "D", "E", 1L); // closeTime in past
        closedPoll.setPollId(2L);

        user = new User("alice", "Alice", "a@a.com", "12345678", "pass");
    }

    // --- vote: new vote ---

    @Test
    void vote_newVote_createsVote() {
        when(pollRepo.findById(1L)).thenReturn(Optional.of(openPoll));
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(voteRepo.findByPollIdAndUsername(1L, "alice")).thenReturn(Optional.empty());
        when(voteRepo.save(any(Vote.class))).thenAnswer(inv -> inv.getArgument(0));

        voteService.vote("alice", 1L, 3);

        verify(voteRepo).save(any(Vote.class));
    }

    // --- vote: update existing vote ---

    @Test
    void vote_existingVote_updatesSelectedOption() {
        Vote existingVote = new Vote("vote-uuid", openPoll, user, 1);
        when(pollRepo.findById(1L)).thenReturn(Optional.of(openPoll));
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(voteRepo.findByPollIdAndUsername(1L, "alice")).thenReturn(Optional.of(existingVote));
        when(voteRepo.save(any(Vote.class))).thenAnswer(inv -> inv.getArgument(0));

        voteService.vote("alice", 1L, 4);

        assertEquals(4, existingVote.getSelectedOption());
        verify(voteRepo).save(existingVote);
    }

    // --- vote: invalid option ---

    @Test
    void vote_optionTooLow_throwsException() {
        when(pollRepo.findById(1L)).thenReturn(Optional.of(openPoll));
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> voteService.vote("alice", 1L, 0));

        assertEquals("Invalid option: must be 1-5", ex.getMessage());
    }

    @Test
    void vote_optionTooHigh_throwsException() {
        when(pollRepo.findById(1L)).thenReturn(Optional.of(openPoll));
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> voteService.vote("alice", 1L, 6));

        assertEquals("Invalid option: must be 1-5", ex.getMessage());
    }

    // --- vote: poll closed ---

    @Test
    void vote_closedPoll_throwsException() {
        when(pollRepo.findById(2L)).thenReturn(Optional.of(closedPoll));
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> voteService.vote("alice", 2L, 1));

        assertEquals("Poll is closed", ex.getMessage());
    }

    // --- vote: poll not found ---

    @Test
    void vote_pollNotFound_throwsException() {
        when(pollRepo.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> voteService.vote("alice", 999L, 1));

        assertTrue(ex.getMessage().contains("Poll not found"));
    }

    // --- vote: user not found ---

    @Test
    void vote_userNotFound_throwsException() {
        when(pollRepo.findById(1L)).thenReturn(Optional.of(openPoll));
        when(userRepo.findById("ghost")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> voteService.vote("ghost", 1L, 1));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    // --- findById ---

    @Test
    void findById_existingVote_returnsVote() {
        Vote vote = new Vote("vote-uuid", openPoll, user, 1);
        when(voteRepo.findById("vote-uuid")).thenReturn(Optional.of(vote));

        Optional<Vote> result = voteService.findById("vote-uuid");

        assertTrue(result.isPresent());
    }

    // --- findByPollIdAndUsername ---

    @Test
    void findByPollIdAndUsername_existing_returnsVote() {
        Vote vote = new Vote("vote-uuid", openPoll, user, 2);
        when(voteRepo.findByPollIdAndUsername(1L, "alice")).thenReturn(Optional.of(vote));

        Optional<Vote> result = voteService.findByPollIdAndUsername(1L, "alice");

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getSelectedOption());
    }

    @Test
    void findByPollIdAndUsername_notVoted_returnsEmpty() {
        when(voteRepo.findByPollIdAndUsername(1L, "bob")).thenReturn(Optional.empty());

        Optional<Vote> result = voteService.findByPollIdAndUsername(1L, "bob");

        assertFalse(result.isPresent());
    }
}
