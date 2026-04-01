package com.hkmu.online_course.service;

import com.hkmu.online_course.model.CourseMaterial;
import java.util.List;

/**
 * Service interface for CourseMaterial operations.
 * Handles file upload/download with Zstandard compression.
 */
public interface ICourseMaterialService {

    CourseMaterial findById(Long materialId);

    List<CourseMaterial> findByLectureId(Long lectureId);

    CourseMaterial upload(Long lectureId, String fileName, String fileExtension,
                         String mimeType, byte[] fileContent);

    byte[] download(Long materialId);

    void deleteById(Long materialId);
}
