package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.service.ILectureService;
import com.hkmu.online_course.service.IPollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for the index/home page.
 * Public endpoint - no authentication required.
 */
@Controller
public class IndexController {

    private final ILectureService lectureService;
    private final IPollService pollService;

    @Autowired
    public IndexController(ILectureService lectureService, IPollService pollService) {
        this.lectureService = lectureService;
        this.pollService = pollService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        List<Lecture> lectures = lectureService.findAll();
        List<Poll> polls = pollService.findOpenPolls();

        model.addAttribute("lectures", lectures);
        model.addAttribute("polls", polls);

        return "index";
    }
}
