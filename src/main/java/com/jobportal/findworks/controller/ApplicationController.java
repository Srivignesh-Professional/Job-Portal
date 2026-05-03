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

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping("/jobs/{jobId}/apply")
    public String apply(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long jobId) {
        jobApplicationService.apply(principal.getUser().getId(), jobId);
        return "redirect:/worker/applications";
    }

    @GetMapping("/worker/applications")
    public String myApplications(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("applications", jobApplicationService.myApplications(principal.getUser().getId()));
        return "applications/my-applications";
    }
}
