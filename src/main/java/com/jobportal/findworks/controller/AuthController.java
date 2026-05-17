package com.jobportal.findworks.controller;

import com.jobportal.findworks.dto.auth.RegisterForm;
import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.AuthService;
import com.jobportal.findworks.service.EmployerProfileService;
import com.jobportal.findworks.service.WorkerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final WorkerProfileService workerProfileService;
    private final EmployerProfileService employerProfileService;

    /*@GetMapping("/")
    public String home() {
        return "public/home";
    }*/

    @GetMapping("/")
    public String home() {
        return "redirect:/jobs";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("form") RegisterForm form,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.register(form);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/register";
        }

        return "redirect:/login?registered";
    }

//    @GetMapping("/dashboard")
//    public String dashboard(Authentication authentication) {
//        // Phase 1: single dashboard view; Phase 2: role-based dashboards
//        return "worker/dashboard";
//    }

    /*@GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal com.jobportal.findworks.security.model.UserPrincipal principal) {
        var user = principal.getUser();

        return switch (user.getRole()) {
            case WORKER -> "redirect:/worker/dashboard";
            case EMPLOYER -> "redirect:/employer/dashboard";
            case ADMIN -> "redirect:/admin/dashboard"; // later
        };
    }*/

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal principal) {
        var user = principal.getUser();

        return switch (user.getRole()) {
            case WORKER -> {
                // if profile not created, force profile creation once
                boolean hasProfile = workerProfileService.profileExists(user.getId());
                yield hasProfile ? "redirect:/jobs" : "redirect:/worker/profile";
            }
            case EMPLOYER -> {
                boolean hasProfile = employerProfileService.profileExists(user.getId());
                yield hasProfile ? "redirect:/employer/dashboard" : "redirect:/employer/profile";
            }
            case ADMIN -> "redirect:/admin/dashboard";
        };
    }
}