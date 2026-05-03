package com.jobportal.findworks.controller;

import com.jobportal.findworks.dto.WorkerProfileForm;
import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.entity.WorkerProfile;
import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.LocationService;
import com.jobportal.findworks.service.WorkerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/worker")
public class WorkerController {

    private final WorkerProfileService workerProfileService;
    private final LocationService locationService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        Long userId = principal.getUser().getId();
        WorkerProfile profile = workerProfileService.findByUserId(userId).orElse(null);

        if (profile == null) {
            return "redirect:/worker/profile";
        }

        model.addAttribute("profile", profile);
        return "worker/dashboard";
    }


    @GetMapping("/profile")
    public String profileForm(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        Long userId = principal.getUser().getId();

        WorkerProfileForm form = new WorkerProfileForm();
        workerProfileService.findByUserId(userId).ifPresent(p -> {
            form.setFullName(p.getFullName());
            form.setAvailability(p.getAvailability());
            form.setPreferredCityIds(
                    p.getPreferredCities().stream().map(c -> c.getId()).toList()
            );
        });

        model.addAttribute("form", form);
        model.addAttribute("cities", locationService.listAllCities());
        model.addAttribute("availabilities", WorkerProfile.Availability.values());
        return "worker/profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@AuthenticationPrincipal UserPrincipal principal,
                              @Valid @ModelAttribute("form") WorkerProfileForm form,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", locationService.listAllCities());
            model.addAttribute("availabilities", WorkerProfile.Availability.values());
            return "worker/profile";
        }

        workerProfileService.upsertProfile(principal.getUser().getId(), form);
        return "redirect:/worker/dashboard";
    }
}