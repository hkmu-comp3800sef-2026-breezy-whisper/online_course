package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.repository.IPollRepository;
import com.hkmu.online_course.repository.IVoteRepository;
import com.hkmu.online_course.service.IPollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of IPollService.
 */
@Service
public class PollServiceImpl implements IPollService {

    private final IPollRepository pollRepo;
    private final IVoteRepository voteRepo;

    @Autowired
    public PollServiceImpl(IPollRepository pollRepo, IVoteRepository voteRepo) {
        this.pollRepo = pollRepo;
        this.voteRepo = voteRepo;
    }

    @Override
    public Optional<Poll> findById(Long pollId) {
        Optional<Poll> poll = pollRepo.findById(pollId);
        poll.ifPresent(p -> p.setVoteCount(voteRepo.countByPollId(pollId)));
        return poll;
    }

    @Override
    public List<Poll> findAll() {
        List<Poll> polls = pollRepo.findAll();
        polls.forEach(poll -> poll.setVoteCount(voteRepo.countByPollId(poll.getPollId())));
        return polls;
    }

    @Override
    public List<Poll> findOpenPolls() {
        List<Poll> polls = pollRepo.findOpenPolls();
        polls.forEach(poll -> poll.setVoteCount(voteRepo.countByPollId(poll.getPollId())));
        return polls;
    }

    @Override
    public Poll create(String question, String option1, String option2, String option3,
                      String option4, String option5, Long closeTime) {
        Poll poll = new Poll(question, option1, option2, option3, option4, option5, closeTime);
        return pollRepo.save(poll);
    }

    @Override
    public void deleteById(Long pollId) {
        if (!pollRepo.existsById(pollId)) {
            throw new IllegalArgumentException("Poll not found: " + pollId);
        }
        pollRepo.deleteById(pollId);
    }
}
