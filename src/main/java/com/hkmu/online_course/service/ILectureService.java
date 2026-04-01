package com.hkmu.online_course.service;

import com.hkmu.online_course.model.Lecture;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Lecture operations.
 */
public interface ILectureService {

    Optional<Lecture> findById(Long lectureId);

    List<Lecture> findAll();

    Lecture create(String title, String summary);

    Lecture update(Long lectureId, String title, String summary);

    void deleteById(Long lectureId);
}
