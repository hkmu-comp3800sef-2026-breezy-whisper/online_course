package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.CourseMaterial;
import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.repository.ICourseMaterialRepository;
import com.hkmu.online_course.repository.ILectureRepository;
import com.hkmu.online_course.service.ICourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

/**
 * Implementation of ICourseMaterialService.
 * Handles file upload/download with Zstandard compression.
 */
@Service
public class CourseMaterialServiceImpl implements ICourseMaterialService {

    private final ICourseMaterialRepository materialRepo;
    private final ILectureRepository lectureRepo;

    @Autowired
    public CourseMaterialServiceImpl(ICourseMaterialRepository materialRepo,
                                    ILectureRepository lectureRepo) {
        this.materialRepo = materialRepo;
        this.lectureRepo = lectureRepo;
    }

    @Override
    public CourseMaterial findById(Long materialId) {
        return materialRepo.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material not found: " + materialId));
    }

    @Override
    public List<CourseMaterial> findByLectureId(Long lectureId) {
        return materialRepo.findByLectureId(lectureId);
    }

    @Override
    public CourseMaterial upload(Long lectureId, String fileName, String fileExtension,
                                 String mimeType, byte[] fileContent) {
        Lecture lecture = lectureRepo.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found: " + lectureId));

        // Compress with Zstandard
        byte[] compressed = compress(fileContent);

        CourseMaterial material = new CourseMaterial(
                lecture,
                fileName,
                fileExtension,
                mimeType,
                compressed,
                (long) fileContent.length
        );

        return materialRepo.save(material);
    }

    @Override
    public byte[] download(Long materialId) {
        CourseMaterial material = findById(materialId);
        byte[] decompressed;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(material.getFileContent());
             ZstdInputStream zis = new ZstdInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            zis.transferTo(baos);
            decompressed = baos.toByteArray();
        } catch (IOException e) {
            // Decompression failed - fall back to raw content
            decompressed = material.getFileContent();
        }
        return decompressed;
    }

    @Override
    public void deleteById(Long materialId) {
        if (!materialRepo.existsById(materialId)) {
            throw new IllegalArgumentException("Material not found: " + materialId);
        }
        materialRepo.deleteById(materialId);
    }

    private byte[] compress(byte[] data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZstdOutputStream zos = new ZstdOutputStream(baos, 3)) {  // Compression level 3
            zos.write(data);
            zos.close();  // Ensure full frame
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to compress data", e);
        }
    }
}

