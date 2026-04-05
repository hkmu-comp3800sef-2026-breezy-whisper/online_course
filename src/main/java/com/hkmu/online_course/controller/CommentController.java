package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Comment;
import com.hkmu.online_course.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for comment operations.
 * All endpoints require authentication.
 */
@Controller
public class CommentController {

    private final ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam Long targetId,
                         @RequestParam String targetType,
                         @RequestParam String content) {
        if (userDetails != null) {
            commentService.create(userDetails.getUsername(), targetId, targetType, content);
        }

        // Redirect back to the target page
        if ("LECTURE".equals(targetType)) {
            return "redirect:/lecture/" + targetId;
        } else {
            return "redirect:/poll/" + targetId;
        }
    }

    @PostMapping("/comment/lecture/{lectureId}")
    @PreAuthorize("isAuthenticated()")
    public String createLectureComment(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable Long lectureId,
                                       @RequestParam String content) {
        if (userDetails != null) {
            commentService.create(userDetails.getUsername(), lectureId, "LECTURE", content);
        }
        return "redirect:/lecture/" + lectureId;
    }

    @PostMapping("/comment/{commentId}/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public String delete(@PathVariable String commentId) {
        Comment comment = commentService.findById(commentId);
        Long targetId = comment.getTargetId();
        String targetType = comment.getTargetType();

        commentService.deleteById(commentId);

        if ("LECTURE".equals(targetType)) {
            return "redirect:/lecture/" + targetId;
        } else {
            return "redirect:/poll/" + targetId;
        }
    }
}
