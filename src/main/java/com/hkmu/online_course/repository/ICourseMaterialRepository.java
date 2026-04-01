package com.hkmu.online_course.repository;

import com.hkmu.online_course.model.CourseMaterial;
import java.util.List;
import java.util.Optional;

/**
 * Clean repository interface for CourseMaterial entity.
 * Service layer depends ONLY on this interface (DIP).
 */
public interface ICourseMaterialRepository {

    Optional<CourseMaterial> findById(Long materialId);

    List<CourseMaterial> findAll();

    long count();

    boolean existsById(Long materialId);

    CourseMaterial save(CourseMaterial material);

    void deleteById(Long materialId);

    // QueryDSL methods
    List<CourseMaterial> findByLectureId(Long lectureId);
}
