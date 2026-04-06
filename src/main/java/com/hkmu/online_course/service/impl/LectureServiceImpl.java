package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Comment;
import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.service.ICourseMaterialService;
import com.hkmu.online_course.repository.ILectureRepository;
import com.hkmu.online_course.service.ICommentService;
import com.hkmu.online_course.service.ILectureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ILectureService.
 */
@Service
public class LectureServiceImpl implements ILectureService {

    private final ILectureRepository lectureRepo;
    private final ICommentService commentService;
    private final ICourseMaterialService materialService;


@Autowired
    public LectureServiceImpl(ILectureRepository lectureRepo, ICommentService commentService, ICourseMaterialService materialService) {
        this.lectureRepo = lectureRepo;
        this.commentService = commentService;
        this.materialService = materialService;
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
    @Transactional
    public void deleteById(Long lectureId) {
        if (!lectureRepo.existsById(lectureId)) {
            throw new IllegalArgumentException("Lecture not found: " + lectureId);
        }
        // Delete materials first to avoid FK constraint
        materialService.findByLectureId(lectureId).forEach(material -> materialService.deleteById(material.getMaterialId()));
        commentService.deleteByTarget(lectureId, Comment.TARGET_TYPE_LECTURE);
        lectureRepo.deleteById(lectureId);
    }

}
