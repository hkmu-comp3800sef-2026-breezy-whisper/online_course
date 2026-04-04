package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.service.ILectureService;
import com.hkmu.online_course.service.IPollService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    private ILectureService lectureService;

    @Mock
    private IPollService pollService;

    @InjectMocks
    private IndexController indexController;

    @Mock
    private Model model;

    @Test
    void index_returnsIndexViewWithLecturesAndPolls() {
        Lecture lecture = new Lecture("Intro to Java", "Learn Java basics");
        lecture.setLectureId(1L);
        Poll poll = new Poll("Best language?", "Java", "Python", "Go", "Rust", "C++", -1L);
        poll.setPollId(1L);
        when(lectureService.findAll()).thenReturn(List.of(lecture));
        when(pollService.findOpenPolls()).thenReturn(List.of(poll));

        String viewName = indexController.index(model);

        assertEquals("index", viewName);
        verify(model).addAttribute("lectures", List.of(lecture));
        verify(model).addAttribute("polls", List.of(poll));
    }

    @Test
    void index_withNoData_returnsEmptyLists() {
        when(lectureService.findAll()).thenReturn(List.of());
        when(pollService.findOpenPolls()).thenReturn(List.of());

        String viewName = indexController.index(model);

        assertEquals("index", viewName);
        verify(model).addAttribute("lectures", List.of());
        verify(model).addAttribute("polls", List.of());
    }
}
