package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.repository.ILectureRepository;
import com.hkmu.online_course.service.ILectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceImplTest {

    @Mock
    private ILectureRepository lectureRepo;

    private ILectureService lectureService;

    @BeforeEach
    void setUp() {
        lectureService = new LectureServiceImpl(lectureRepo);
    }

    // --- findById ---

    @Test
    void findById_existingLecture_returnsLecture() {
        Lecture lecture = new Lecture("Intro to Java", "Basic Java programming");
        when(lectureRepo.findById(1L)).thenReturn(Optional.of(lecture));

        Optional<Lecture> result = lectureService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Intro to Java", result.get().getTitle());
    }

    @Test
    void findById_nonExistingLecture_returnsEmpty() {
        when(lectureRepo.findById(999L)).thenReturn(Optional.empty());

        Optional<Lecture> result = lectureService.findById(999L);

        assertFalse(result.isPresent());
    }

    // --- findAll ---

    @Test
    void findAll_returnsAllLectures() {
        Lecture l1 = new Lecture("Lecture 1", "Summary 1");
        Lecture l2 = new Lecture("Lecture 2", "Summary 2");
        when(lectureRepo.findAll()).thenReturn(List.of(l1, l2));

        List<Lecture> result = lectureService.findAll();

        assertEquals(2, result.size());
    }

    // --- create ---

    @Test
    void create_validData_savesAndReturnsLecture() {
        when(lectureRepo.save(any(Lecture.class))).thenAnswer(inv -> {
            Lecture l = inv.getArgument(0);
            l.setLectureId(1L);
            return l;
        });

        Lecture result = lectureService.create("New Lecture", "New Summary");

        assertNotNull(result);
        assertEquals("New Lecture", result.getTitle());
        assertEquals("New Summary", result.getSummary());
        verify(lectureRepo).save(any(Lecture.class));
    }

    // --- update ---

    @Test
    void update_existingLecture_updatesAndSaves() {
        Lecture lecture = new Lecture("Old Title", "Old Summary");
        lecture.setLectureId(1L);
        when(lectureRepo.findById(1L)).thenReturn(Optional.of(lecture));
        when(lectureRepo.save(any(Lecture.class))).thenAnswer(inv -> inv.getArgument(0));

        Lecture result = lectureService.update(1L, "Updated Title", "Updated Summary");

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Summary", result.getSummary());
    }

    @Test
    void update_nonExistingLecture_throwsException() {
        when(lectureRepo.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> lectureService.update(999L, "Title", "Summary"));

        assertTrue(ex.getMessage().contains("Lecture not found"));
    }

    // --- deleteById ---

    @Test
    void deleteById_existingLecture_deletes() {
        when(lectureRepo.existsById(1L)).thenReturn(true);
        doNothing().when(lectureRepo).deleteById(1L);

        lectureService.deleteById(1L);

        verify(lectureRepo).deleteById(1L);
    }

    @Test
    void deleteById_nonExistingLecture_throwsException() {
        when(lectureRepo.existsById(999L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> lectureService.deleteById(999L));

        assertTrue(ex.getMessage().contains("Lecture not found"));
        verify(lectureRepo, never()).deleteById(any());
    }
}
