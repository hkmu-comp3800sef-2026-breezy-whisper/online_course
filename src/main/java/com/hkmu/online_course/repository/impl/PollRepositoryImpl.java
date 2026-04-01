package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.QPoll;
import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.repository.IPollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL-based implementation of IPollRepository.
 * Delegates CRUD to PollJpaSpringRepo, uses JPAQueryFactory for complex queries.
 */
@Repository
public class PollRepositoryImpl extends AbstractRepositoryImpl implements IPollRepository {

    private final PollJpaSpringRepo springRepo;

    @Autowired
    public PollRepositoryImpl(PollJpaSpringRepo springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Poll> findById(Long pollId) {
        return springRepo.findById(pollId);
    }

    @Override
    public List<Poll> findAll() {
        return springRepo.findAll();
    }

    @Override
    public long count() {
        return springRepo.count();
    }

    @Override
    public boolean existsById(Long pollId) {
        return springRepo.existsById(pollId);
    }

    @Override
    public Poll save(Poll poll) {
        return springRepo.save(poll);
    }

    @Override
    public void deleteById(Long pollId) {
        springRepo.deleteById(pollId);
    }

    // QueryDSL methods
    @Override
    public List<Poll> findOpenPolls() {
        QPoll poll = QPoll.poll;
        return queryFactory
                .selectFrom(poll)
                .where(poll.closeTime.eq(-1L).or(poll.closeTime.gt(System.currentTimeMillis())))
                .fetch();
    }
}
