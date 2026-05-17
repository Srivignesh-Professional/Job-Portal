package com.jobportal.findworks.common.exception;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String accessDenied(Model model) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", "You are not allowed to access this page.");
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    public String generic(Model model, Exception ex) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", ex.getMessage());
        return "error/500";
    }
}