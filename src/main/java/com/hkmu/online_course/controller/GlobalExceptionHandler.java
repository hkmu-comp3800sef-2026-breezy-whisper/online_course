package com.hkmu.online_course.controller;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceeded(HttpServletRequest request, MaxUploadSizeExceededException e, Model model) {
        model.addAttribute("error", "File too large. Maximum size is 10MB. Please upload smaller file.");
        model.addAttribute("uploadError", true);
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/lecture/")) {
            String[] parts = referer.split("/lecture/");
            if (parts.length > 1) {
                String lecturePart = parts[1].split("/")[0];
                try {
                    Long lectureId = Long.valueOf(lecturePart);
                    model.addAttribute("lectureId", lectureId);
                } catch (NumberFormatException ignored) {}
            }
        }
        return "lecture/material/create";
    }

    private Long getLectureIdFromRequest(MaxUploadSizeExceededException e) {
        // Extract lectureId from request if available, fallback to null
        // This requires access to request attributes - simplified for now
        return null;
    }
}

