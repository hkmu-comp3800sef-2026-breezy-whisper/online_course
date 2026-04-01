package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.QUser;
import com.hkmu.online_course.model.User;
import com.hkmu.online_course.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL-based implementation of IUserRepository.
 * Delegates CRUD to UserJpaSpringRepo, uses JPAQueryFactory for complex queries.
 * This is the ONLY class Service should ever depend on for User access.
 */
@Repository
public class UserRepositoryImpl extends AbstractRepositoryImpl implements IUserRepository {

    private final UserJpaSpringRepo springRepo;

    @Autowired
    public UserRepositoryImpl(UserJpaSpringRepo springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<User> findById(String username) {
        return springRepo.findById(username);
    }

    @Override
    public List<User> findAll() {
        return springRepo.findAll();
    }

    @Override
    public long count() {
        return springRepo.count();
    }

    @Override
    public boolean existsById(String username) {
        return springRepo.existsById(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return springRepo.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springRepo.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return springRepo.save(user);
    }

    @Override
    public void deleteById(String username) {
        springRepo.deleteById(username);
    }

    // QueryDSL methods
    @Override
    public List<User> findByRole(Integer role) {
        QUser user = QUser.user;
        return queryFactory
                .selectFrom(user)
                .where(user.role.eq(role))
                .fetch();
    }

    @Override
    public List<User> findByStatus(Integer status) {
        QUser user = QUser.user;
        return queryFactory
                .selectFrom(user)
                .where(user.status.eq(status))
                .fetch();
    }
}
