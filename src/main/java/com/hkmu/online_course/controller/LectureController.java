package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Lecture;
import com.hkmu.online_course.service.ILectureService;
import com.hkmu.online_course.service.ICourseMaterialService;
import com.hkmu.online_course.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Lecture operations.
 *
 * Security is enforced at METHOD LEVEL via @PreAuthorize:
 * - create(), edit(), update(), delete() require TEACHER role
 * - list(), view() only require authentication (any role)
 */
@Controller
@RequestMapping("/lecture")
public class LectureController {

    private final ILectureService lectureService;
    private final ICourseMaterialService materialService;
    private final ICommentService commentService;

    @Autowired
    public LectureController(ILectureService lectureService,
                             ICourseMaterialService materialService,
                             ICommentService commentService) {
        this.lectureService = lectureService;
        this.materialService = materialService;
        this.commentService = commentService;
    }

    // ========== Public/Authenticated User Endpoints ==========

    @GetMapping("/list")
    public String list(Model model) {
        List<Lecture> lectures = lectureService.findAll();
        model.addAttribute("lectures", lectures);
        return "lecture/list";
    }

    @GetMapping("/{lectureId}")
    public String view(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found: " + lectureId));
        model.addAttribute("lecture", lecture);
        model.addAttribute("materials", materialService.findByLectureId(lectureId));
        model.addAttribute("comments", commentService.findByTarget(lectureId, "LECTURE"));
        return "lecture/view";
    }

    // ========== Teacher Only Endpoints ==========

    @GetMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createForm() {
        return "lecture/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String create(@RequestParam String title, @RequestParam String summary) {
        lectureService.create(title, summary);
        return "redirect:/lecture/list";
    }

    @GetMapping("/{lectureId}/edit")
    @PreAuthorize("hasRole('TEACHER')")
    public String editForm(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found: " + lectureId));
        model.addAttribute("lecture", lecture);
        return "lecture/edit";
    }

    @PostMapping("/{lectureId}/edit")
    @PreAuthorize("hasRole('TEACHER')")
    public String edit(@PathVariable Long lectureId,
                      @RequestParam String title,
                      @RequestParam String summary) {
        lectureService.update(lectureId, title, summary);
        return "redirect:/lecture/" + lectureId;
    }

    @PostMapping("/{lectureId}/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public String delete(@PathVariable Long lectureId) {
        lectureService.deleteById(lectureId);
        return "redirect:/lecture/list";
    }

    // ========== Course Material Endpoints ==========

    @GetMapping("/{lectureId}/material/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createMaterialForm(@PathVariable Long lectureId, Model model) {
        model.addAttribute("lectureId", lectureId);
        return "lecture/material/create";
    }

    @PostMapping("/{lectureId}/material/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createMaterial(@PathVariable Long lectureId,
                                @RequestParam String fileName,
                                @RequestParam String fileExtension,
                                @RequestParam String mimeType,
                                @RequestParam byte[] fileContent) {
        materialService.upload(lectureId, fileName, fileExtension, mimeType, fileContent);
        return "redirect:/lecture/" + lectureId;
    }

    @PostMapping("/{lectureId}/material/{materialId}/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteMaterial(@PathVariable Long lectureId,
                                  @PathVariable Long materialId) {
        materialService.deleteById(materialId);
        return "redirect:/lecture/" + lectureId;
    }
}
