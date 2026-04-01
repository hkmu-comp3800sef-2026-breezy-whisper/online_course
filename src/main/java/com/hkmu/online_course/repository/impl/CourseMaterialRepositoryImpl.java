package com.hkmu.online_course.repository.impl;

import com.hkmu.online_course.model.QCourseMaterial;
import com.hkmu.online_course.model.QLecture;
import com.hkmu.online_course.model.CourseMaterial;
import com.hkmu.online_course.repository.ICourseMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL-based implementation of ICourseMaterialRepository.
 * Delegates CRUD to CourseMaterialJpaSpringRepo, uses JPAQueryFactory for complex queries.
 */
@Repository
public class CourseMaterialRepositoryImpl extends AbstractRepositoryImpl implements ICourseMaterialRepository {

    private final CourseMaterialJpaSpringRepo springRepo;

    @Autowired
    public CourseMaterialRepositoryImpl(CourseMaterialJpaSpringRepo springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<CourseMaterial> findById(Long materialId) {
        return springRepo.findById(materialId);
    }

    @Override
    public List<CourseMaterial> findAll() {
        return springRepo.findAll();
    }

    @Override
    public long count() {
        return springRepo.count();
    }

    @Override
    public boolean existsById(Long materialId) {
        return springRepo.existsById(materialId);
    }

    @Override
    public CourseMaterial save(CourseMaterial material) {
        return springRepo.save(material);
    }

    @Override
    public void deleteById(Long materialId) {
        springRepo.deleteById(materialId);
    }

    // QueryDSL methods
    @Override
    public List<CourseMaterial> findByLectureId(Long lectureId) {
        QCourseMaterial material = QCourseMaterial.courseMaterial;
        return queryFactory
                .selectFrom(material)
                .join(material.lecture, QLecture.lecture)
                .where(QLecture.lecture.lectureId.eq(lectureId))
                .fetch();
    }
}
