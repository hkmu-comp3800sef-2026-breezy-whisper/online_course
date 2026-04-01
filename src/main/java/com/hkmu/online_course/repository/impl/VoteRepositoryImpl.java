package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.QPoll;
import com.hkmu.online_course.model.QUser;
import com.hkmu.online_course.model.QVote;
import com.hkmu.online_course.model.Vote;
import com.hkmu.online_course.repository.IVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL-based implementation of IVoteRepository.
 * Delegates CRUD to VoteJpaSpringRepo, uses JPAQueryFactory for complex queries.
 */
@Repository
public class VoteRepositoryImpl extends AbstractRepositoryImpl implements IVoteRepository {

    private final VoteJpaSpringRepo springRepo;

    @Autowired
    public VoteRepositoryImpl(VoteJpaSpringRepo springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Vote> findById(String voteId) {
        return springRepo.findById(voteId);
    }

    @Override
    public List<Vote> findAll() {
        return springRepo.findAll();
    }

    @Override
    public long count() {
        return springRepo.count();
    }

    @Override
    public boolean existsById(String voteId) {
        return springRepo.existsById(voteId);
    }

    @Override
    public Vote save(Vote vote) {
        return springRepo.save(vote);
    }

    @Override
    public void deleteById(String voteId) {
        springRepo.deleteById(voteId);
    }

    // QueryDSL methods
    @Override
    public List<Vote> findByPollId(Long pollId) {
        QVote vote = QVote.vote;
        return queryFactory
                .selectFrom(vote)
                .join(vote.poll, QPoll.poll)
                .where(QPoll.poll.pollId.eq(pollId))
                .fetch();
    }

    @Override
    public long countByPollId(Long pollId) {
        QVote vote = QVote.vote;
        return queryFactory
                .selectFrom(vote)
                .join(vote.poll, QPoll.poll)
                .where(QPoll.poll.pollId.eq(pollId))
                .fetchCount();
    }

    @Override
    public Optional<Vote> findByPollIdAndUsername(Long pollId, String username) {
        QVote vote = QVote.vote;
        return Optional.ofNullable(queryFactory
                .selectFrom(vote)
                .join(vote.poll, QPoll.poll)
                .join(vote.user, QUser.user)
                .where(QPoll.poll.pollId.eq(pollId)
                        .and(QUser.user.username.eq(username)))
                .fetchOne());
    }

    @Override
    public boolean existsByPollIdAndUsername(Long pollId, String username) {
        QVote vote = QVote.vote;
        return queryFactory
                .selectFrom(vote)
                .join(vote.poll, QPoll.poll)
                .join(vote.user, QUser.user)
                .where(QPoll.poll.pollId.eq(pollId)
                        .and(QUser.user.username.eq(username)))
                .fetchFirst() != null;
    }
}
