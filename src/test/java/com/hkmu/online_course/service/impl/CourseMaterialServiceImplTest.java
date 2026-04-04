package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.CourseMaterial;
import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.repository.ICourseMaterialRepository;
import com.hkmu.online_course.repository.ILectureRepository;
import com.hkmu.online_course.service.ICourseMaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

@ExtendWith(MockitoExtension.class)
class CourseMaterialServiceImplTest {

    @Mock
    private ICourseMaterialRepository materialRepo;

    @Mock
    private ILectureRepository lectureRepo;

    private ICourseMaterialService materialService;

    private Lecture lecture;

    @BeforeEach
    void setUp() {
        materialService = new CourseMaterialServiceImpl(materialRepo, lectureRepo);
        lecture = new Lecture("Test Lecture", "Test Summary");
        lecture.setLectureId(1L);
    }

    // --- findById ---

    @Test
    void findById_existingMaterial_returnsMaterial() {
        CourseMaterial material = new CourseMaterial(lecture, "notes.pdf", ".pdf", "application/pdf", new byte[]{1,2,3}, 3L);
        material.setMaterialId(1L);
        when(materialRepo.findById(1L)).thenReturn(Optional.of(material));

        CourseMaterial result = materialService.findById(1L);

        assertNotNull(result);
        assertEquals("notes.pdf", result.getFileName());
    }

    @Test
    void findById_nonExistingMaterial_throwsException() {
        when(materialRepo.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> materialService.findById(999L));

        assertTrue(ex.getMessage().contains("Material not found"));
    }

    // --- findByLectureId ---

    @Test
    void findByLectureId_returnsMaterialsForLecture() {
        CourseMaterial m1 = new CourseMaterial(lecture, "notes.pdf", ".pdf", "application/pdf", new byte[]{1}, 1L);
        m1.setMaterialId(1L);
        CourseMaterial m2 = new CourseMaterial(lecture, "slides.pdf", ".pdf", "application/pdf", new byte[]{2}, 1L);
        m2.setMaterialId(2L);
        when(materialRepo.findByLectureId(1L)).thenReturn(List.of(m1, m2));

        List<CourseMaterial> result = materialService.findByLectureId(1L);

        assertEquals(2, result.size());
    }

    // --- upload ---

    @Test
    void upload_validFile_compressesAndSaves() throws Exception {
        byte[] originalContent = "Hello World Content".getBytes();
        when(lectureRepo.findById(1L)).thenReturn(Optional.of(lecture));
        when(materialRepo.save(any(CourseMaterial.class))).thenAnswer(inv -> {
            CourseMaterial m = inv.getArgument(0);
            m.setMaterialId(1L);
            return m;
        });

        CourseMaterial result = materialService.upload(1L, "test.txt", ".txt", "text/plain", originalContent);

        assertNotNull(result);
        assertEquals("test.txt", result.getFileName());
        assertEquals(".txt", result.getFileExtension());
        assertEquals("text/plain", result.getMimeType());
        assertEquals((long) originalContent.length, result.getFileSize());
        // Verify content is actually compressed (compressed != original)
        assertFalse(java.util.Arrays.equals(originalContent, result.getFileContent()));
        verify(materialRepo).save(any(CourseMaterial.class));
    }

    @Test
    void upload_lectureNotFound_throwsException() {
        when(lectureRepo.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> materialService.upload(999L, "test.txt", ".txt", "text/plain", new byte[]{1}));

        assertTrue(ex.getMessage().contains("Lecture not found"));
    }

    // --- download ---

    @Test
    void download_existingMaterial_decompressesAndReturnsOriginalContent() throws Exception {
        // Create compressed content manually
        byte[] originalContent = "Download Test Content".getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZstdOutputStream zos = new ZstdOutputStream(baos)) {
            zos.write(originalContent);
            zos.flush();
        }
        byte[] compressedContent = baos.toByteArray();

        CourseMaterial material = new CourseMaterial(lecture, "test.txt", ".txt", "text/plain", compressedContent, (long) originalContent.length);
        material.setMaterialId(1L);
        when(materialRepo.findById(1L)).thenReturn(Optional.of(material));

        byte[] result = materialService.download(1L);

        assertArrayEquals(originalContent, result);
    }

    // --- deleteById ---

    @Test
    void deleteById_existingMaterial_deletes() {
        when(materialRepo.existsById(1L)).thenReturn(true);
        doNothing().when(materialRepo).deleteById(1L);

        materialService.deleteById(1L);

        verify(materialRepo).deleteById(1L);
    }

    @Test
    void deleteById_nonExistingMaterial_throwsException() {
        when(materialRepo.existsById(999L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> materialService.deleteById(999L));

        assertTrue(ex.getMessage().contains("Material not found"));
        verify(materialRepo, never()).deleteById(any());
    }
}
