package com.jobportal.findworks.controller;

import com.jobportal.findworks.dto.EmployerProfileForm;
import com.jobportal.findworks.entity.EmployerProfile;
import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.EmployerProfileService;
import com.jobportal.findworks.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {

    private final EmployerProfileService employerProfileService;
    private final LocationService locationService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        Long userId = principal.getUser().getId();
        EmployerProfile profile = employerProfileService.findByUserId(userId).orElse(null);

        if (profile == null) {
            return "redirect:/employer/profile";
        }

        model.addAttribute("profile", profile);
        return "employer/dashboard";
    }

    @GetMapping("/profile")
    public String profileForm(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        Long userId = principal.getUser().getId();

        EmployerProfileForm form = new EmployerProfileForm();
        employerProfileService.findByUserId(userId).ifPresent(p -> {
            form.setCompanyName(p.getCompanyName());
            form.setContactName(p.getContactName());
            form.setAddressLine(p.getAddressLine());
            if (p.getCity() != null) form.setCityId(p.getCity().getId());
        });

        model.addAttribute("form", form);
        model.addAttribute("cities", locationService.listAllCities());
        return "employer/profile";
    }


    @PostMapping("/profile")
    public String saveProfile(@AuthenticationPrincipal UserPrincipal principal,
                              @Valid @ModelAttribute("form") EmployerProfileForm form,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", locationService.listAllCities());
            return "employer/profile";
        }

        // Pass userId, not principal.getUser()
        employerProfileService.upsertProfile(principal.getUser().getId(), form);

        return "redirect:/employer/dashboard";
    }
}
