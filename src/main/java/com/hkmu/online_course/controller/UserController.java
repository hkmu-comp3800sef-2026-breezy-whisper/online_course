package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Comment;
import com.hkmu.online_course.model.User;
import com.hkmu.online_course.service.IUserService;
import com.hkmu.online_course.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for user profile and comment history.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;
    private final ICommentService commentService;

    @Autowired
    public UserController(IUserService userService, ICommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String phoneNumber) {
        userService.updateProfile(userDetails.getUsername(), fullName, email, phoneNumber);
        return "redirect:/user/profile?updated";
    }

    @GetMapping("/comments")
    @PreAuthorize("isAuthenticated()")
    public String comments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Comment> comments = commentService.findByUsername(userDetails.getUsername());
        model.addAttribute("comments", comments);
        return "user/comments";
    }
}
