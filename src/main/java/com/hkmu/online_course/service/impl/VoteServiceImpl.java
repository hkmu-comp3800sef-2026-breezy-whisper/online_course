package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.model.User;
import com.hkmu.online_course.model.Vote;
import com.hkmu.online_course.repository.IPollRepository;
import com.hkmu.online_course.repository.IUserRepository;
import com.hkmu.online_course.repository.IVoteRepository;
import com.hkmu.online_course.service.IVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of IVoteService.
 */
@Service
public class VoteServiceImpl implements IVoteService {

    private final IVoteRepository voteRepo;
    private final IPollRepository pollRepo;
    private final IUserRepository userRepo;

    @Autowired
    public VoteServiceImpl(IVoteRepository voteRepo, IPollRepository pollRepo,
                          IUserRepository userRepo) {
        this.voteRepo = voteRepo;
        this.pollRepo = pollRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Optional<Vote> findById(String voteId) {
        return voteRepo.findById(voteId);
    }

    @Override
    public List<Vote> findByPollId(Long pollId) {
        return voteRepo.findByPollId(pollId);
    }

    @Override
    public Optional<Vote> findByPollIdAndUsername(Long pollId, String username) {
        return voteRepo.findByPollIdAndUsername(pollId, username);
    }

@Override
    @Transactional
    public void vote(String username, Long pollId, Integer selectedOption) {
        Poll poll = pollRepo.findById(pollId)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found: " + pollId));

        User user = userRepo.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Check poll is open
        if (!poll.isOpen()) {
            throw new IllegalArgumentException("Poll is closed");
        }

        // Validate option
        if (selectedOption < 1 || selectedOption > 5) {
            throw new IllegalArgumentException("Invalid option: must be 1-5");
        }

        // Check if already voted - update existing or create new
        Optional<Vote> existingVote = voteRepo.findByPollIdAndUsername(pollId, username);
        if (existingVote.isPresent()) {
            // Update existing vote
            existingVote.get().setSelectedOption(selectedOption);
            voteRepo.save(existingVote.get());
        } else {
            // Create new vote
            Vote vote = new Vote(UUID.randomUUID().toString(), poll, user, selectedOption);
            voteRepo.save(vote);
        }
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        voteRepo.findByUsername(username).forEach(vote -> voteRepo.deleteById(vote.getVoteId()));
    }
}
