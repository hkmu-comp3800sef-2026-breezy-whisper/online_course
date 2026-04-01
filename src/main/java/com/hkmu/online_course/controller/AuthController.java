package com.hkmu.online_course.controller;

import com.hkmu.online_course.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for authentication (login/register).
 * All endpoints are public - security handled by Spring Security.
 */
@Controller
public class AuthController {

    private final IUserService userService;

    @Autowired
    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String fullName,
                                  @RequestParam String email,
                                  @RequestParam String phoneNumber,
                                  @RequestParam(defaultValue = "student") String role) {
        try {
            if ("teacher".equals(role)) {
                userService.registerTeacher(username, password, fullName, email, phoneNumber);
            } else {
                userService.registerStudent(username, password, fullName, email, phoneNumber);
            }
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            return "redirect:/register?error=" + e.getMessage();
        }
    }
}
