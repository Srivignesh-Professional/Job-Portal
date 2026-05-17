package com.jobportal.findworks.controller;

import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.impl.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    private final JobApplicationService jobApplicationService;


    @PostMapping("/jobs/{jobId}/apply")
    public String apply(@PathVariable Long jobId,
                        @AuthenticationPrincipal UserPrincipal principal,
                        RedirectAttributes ra) {
        try {
            jobApplicationService.apply(jobId, principal.getUser().getId());
            ra.addFlashAttribute("success", "Applied successfully.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/jobs/" + jobId;
    }

    @GetMapping("/worker/applications")
    public String myApplications(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("applications", jobApplicationService.myApplications(principal.getUser().getId()));
        return "applications/my-applications";
    }
}
