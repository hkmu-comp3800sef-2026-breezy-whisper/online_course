package com.hkmu.online_course.repository;

import com.hkmu.online_course.model.Lecture;
import java.util.List;
import java.util.Optional;

/**
 * Clean repository interface for Lecture entity.
 * Service layer depends ONLY on this interface (DIP).
 */
public interface ILectureRepository {

    Optional<Lecture> findById(Long lectureId);

    List<Lecture> findAll();

    long count();

    boolean existsById(Long lectureId);

    Lecture save(Lecture lecture);

    void deleteById(Long lectureId);
}
