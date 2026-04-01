package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.repository.ILectureRepository;
import com.hkmu.online_course.service.ILectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ILectureService.
 */
@Service
public class LectureServiceImpl implements ILectureService {

    private final ILectureRepository lectureRepo;

    @Autowired
    public LectureServiceImpl(ILectureRepository lectureRepo) {
        this.lectureRepo = lectureRepo;
    }

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        return lectureRepo.findById(lectureId);
    }

    @Override
    public List<Lecture> findAll() {
        return lectureRepo.findAll();
    }

    @Override
    public Lecture create(String title, String summary) {
        Lecture lecture = new Lecture(title, summary);
        return lectureRepo.save(lecture);
    }

    @Override
    public Lecture update(Long lectureId, String title, String summary) {
        Lecture lecture = lectureRepo.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found: " + lectureId));

        lecture.setTitle(title);
        lecture.setSummary(summary);

        return lectureRepo.save(lecture);
    }

    @Override
    public void deleteById(Long lectureId) {
        if (!lectureRepo.existsById(lectureId)) {
            throw new IllegalArgumentException("Lecture not found: " + lectureId);
        }
        lectureRepo.deleteById(lectureId);
    }
}
