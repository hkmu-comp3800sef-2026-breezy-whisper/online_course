package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PreAuthorize("hasRole('TEACHER')")
    public String listUsers(Model model) {
        List<User> allUsers = userService.findAll();
        model.addAttribute("users", allUsers);
        return "admin/users";
    }


    @GetMapping("/users/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createUserForm(Model model) {
        model.addAttribute("adminRegister", true);
        return "register";
    }



    @GetMapping("/users/{username}/view")
    @PreAuthorize("hasRole('TEACHER')")
    public String viewUser(@PathVariable String username, Model model) {
        User user = userService.findById(username).orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        model.addAttribute("user", user);
        model.addAttribute("adminView", true);
        model.addAttribute("targetUsername", username);
        return "user/profile";
    }


    @PostMapping("/users/{username}/update")
    @PreAuthorize("hasRole('TEACHER')")
    public String updateUser(@PathVariable String username,
                            @RequestParam(required = false) String newUsername,
                            String fullName, String email, String phoneNumber, 
                            Model model) {
        try {
            if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(username)) {
                userService.changeUsername(username, newUsername);
                return "redirect:/admin/users/" + newUsername + "/view?updated=true";
            } else {
                userService.updateProfile(username, fullName, email, phoneNumber);
                return "redirect:/admin/users/" + username + "/view?updated=true";
            }
        } catch (IllegalArgumentException e) {
            String errorMsg = e.getMessage();
            return "redirect:/admin/users/" + username + "/view?error=" + java.net.URLEncoder.encode(errorMsg, java.nio.charset.StandardCharsets.UTF_8);
        }
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

    @PostMapping("/users/{username}/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteUser(@PathVariable String username, @AuthenticationPrincipal UserDetails principal) {
        userService.deleteById(username, principal.getUsername());
        return "redirect:/admin/users?deleted=true";
    }
}

