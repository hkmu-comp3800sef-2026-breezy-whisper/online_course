package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for admin/user management operations.
 * All endpoints require TEACHER role via @PreAuthorize.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('TEACHER')")
public class AdminController {

    private final IUserService userService;

    @Autowired
    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> allUsers = userService.findAll();
        model.addAttribute("users", allUsers);
        return "admin/users";
    }

    @PostMapping("/users/{username}/activate")
    @PreAuthorize("hasRole('TEACHER')")
    public String activateTeacher(@PathVariable String username) {
        userService.activateTeacher(username);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{username}/disable")
    @PreAuthorize("hasRole('TEACHER')")
    public String disableUser(@PathVariable String username,
                               @RequestParam(required = false) String reason) {
        userService.disableUser(username, reason);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{username}/enable")
    @PreAuthorize("hasRole('TEACHER')")
    public String enableUser(@PathVariable String username) {
        userService.enableUser(username);
        return "redirect:/admin/users";
    }
}
