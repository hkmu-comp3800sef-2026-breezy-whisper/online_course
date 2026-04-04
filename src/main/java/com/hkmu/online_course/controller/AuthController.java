package com.hkmu.online_course.controller;

import com.hkmu.online_course.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

/**
 * Controller for authentication (login/register).
 * All endpoints are public - security handled by Spring Security.
 */
@Controller
public class AuthController {

    private final IUserService userService;
    private final MessageSource messageSource;

    @Autowired
    public AuthController(IUserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/test-i18n")
    @ResponseBody
    public String testI18n() {
        Locale locale = LocaleContextHolder.getLocale();
        String title = messageSource.getMessage("login.title", null, locale);
        return "Current locale: " + locale + " | login.title = " + title;
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
