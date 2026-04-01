package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.repository.ILectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL-based implementation of ILectureRepository.
 * Delegates CRUD to LectureJpaSpringRepo, uses JPAQueryFactory for complex queries.
 */
@Repository
public class LectureRepositoryImpl extends AbstractRepositoryImpl implements ILectureRepository {

    private final LectureJpaSpringRepo springRepo;

    @Autowired
    public LectureRepositoryImpl(LectureJpaSpringRepo springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        return springRepo.findById(lectureId);
    }

    @Override
    public List<Lecture> findAll() {
        return springRepo.findAll();
    }

    @Override
    public long count() {
        return springRepo.count();
    }

    @Override
    public boolean existsById(Long lectureId) {
        return springRepo.existsById(lectureId);
    }

    @Override
    public Lecture save(Lecture lecture) {
        return springRepo.save(lecture);
    }

    @Override
    public void deleteById(Long lectureId) {
        springRepo.deleteById(lectureId);
    }

    // ========== QueryDSL query methods (add when needed) ==========
    //
    // public List<Lecture> findByTitleContaining(String keyword) {
    //     QLecture lecture = QLecture.lecture;
    //     return queryFactory
    //         .selectFrom(lecture)
    //         .where(lecture.title.contains(keyword))
    //         .fetch();
    // }
}
