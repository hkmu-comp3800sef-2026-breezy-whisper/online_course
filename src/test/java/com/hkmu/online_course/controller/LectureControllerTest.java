package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.service.ICourseMaterialService;
import com.hkmu.online_course.service.ICommentService;
import com.hkmu.online_course.service.ILectureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureControllerTest {

    @Mock
    private ILectureService lectureService;

    @Mock
    private ICourseMaterialService materialService;

    @Mock
    private ICommentService commentService;

    @InjectMocks
    private LectureController lectureController;

    @Mock
    private Model model;

    @Test
    void list_returnsLectureListView() {
        Lecture lecture = new Lecture("Lecture 1", "Summary");
        lecture.setLectureId(1L);
        when(lectureService.findAll()).thenReturn(List.of(lecture));

        String viewName = lectureController.list(model);

        assertEquals("lecture/list", viewName);
        verify(model).addAttribute("lectures", List.of(lecture));
    }

    @Test
    void view_existingLecture_returnsLectureView() {
        Lecture lecture = new Lecture("Java Basics", "Intro to Java");
        lecture.setLectureId(1L);
        when(lectureService.findById(1L)).thenReturn(Optional.of(lecture));
        when(materialService.findByLectureId(1L)).thenReturn(List.of());
        when(commentService.findByTarget(1L, "LECTURE")).thenReturn(List.of());

        String viewName = lectureController.view(1L, model);

        assertEquals("lecture/view", viewName);
        verify(model).addAttribute("lecture", lecture);
        verify(model).addAttribute("materials", List.of());
        verify(model).addAttribute("comments", List.of());
    }

    @Test
    void createForm_returnsCreateView() {
        String viewName = lectureController.createForm();
        assertEquals("lecture/create", viewName);
    }

    @Test
    void createSubmit_validLecture_redirectsToList() {
        Lecture created = new Lecture("New Lecture", "New Summary");
        created.setLectureId(1L);
        when(lectureService.create("New Lecture", "New Summary")).thenReturn(created);

        String result = lectureController.create("New Lecture", "New Summary");

        assertEquals("redirect:/lecture/list", result);
        verify(lectureService).create("New Lecture", "New Summary");
    }

    @Test
    void editForm_existingLecture_returnsEditView() {
        Lecture lecture = new Lecture("Old Title", "Old Summary");
        lecture.setLectureId(1L);
        when(lectureService.findById(1L)).thenReturn(Optional.of(lecture));

        String viewName = lectureController.editForm(1L, model);

        assertEquals("lecture/edit", viewName);
        verify(model).addAttribute("lecture", lecture);
    }

    @Test
    void editSubmit_validUpdate_redirectsToLectureView() {
        when(lectureService.update(1L, "Updated Title", "Updated Summary")).thenReturn(new Lecture("Updated Title", "Updated Summary"));

        String result = lectureController.edit(1L, "Updated Title", "Updated Summary");

        assertEquals("redirect:/lecture/1", result);
        verify(lectureService).update(1L, "Updated Title", "Updated Summary");
    }

    @Test
    void delete_existingLecture_redirectsToList() {
        doNothing().when(lectureService).deleteById(1L);

        String result = lectureController.delete(1L);

        assertEquals("redirect:/lecture/list", result);
        verify(lectureService).deleteById(1L);
    }

    @Test
    void createMaterialForm_returnsMaterialCreateView() {
        String viewName = lectureController.createMaterialForm(1L, model);
        assertEquals("lecture/material/create", viewName);
        verify(model).addAttribute("lectureId", 1L);
    }

    @Test
    void deleteMaterial_existingMaterial_redirectsToLecture() {
        doNothing().when(materialService).deleteById(1L);

        String result = lectureController.deleteMaterial(1L, 1L);

        assertEquals("redirect:/lecture/1", result);
        verify(materialService).deleteById(1L);
    }
}
