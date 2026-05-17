package com.jobportal.findworks.controller;

import com.jobportal.findworks.entity.application.JobApplication;
import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.impl.JobApplicationService;
import com.jobportal.findworks.service.impl.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/jobs")
public class EmployerApplicantsController {

    private final JobApplicationService jobApplicationService;
    private final JobPostService jobPostService;

    @GetMapping("/{jobId}/applications")
    public String applicants(@AuthenticationPrincipal UserPrincipal principal,
                             @PathVariable Long jobId,
                             Model model) {

        Long employerId = principal.getUser().getId();

        model.addAttribute("job", jobPostService.getOrThrow(jobId));
        model.addAttribute("applicants", jobApplicationService.listApplicantsForEmployer(employerId, jobId));
        model.addAttribute("statuses", JobApplication.Status.values());

        return "applications/applicants";
    }

    /*@PostMapping("/{jobId}/applications/{appId}/status")
    public String updateStatus(@AuthenticationPrincipal UserPrincipal principal,
                               @PathVariable Long jobId,
                               @PathVariable Long appId,
                               @RequestParam JobApplication.Status status) {

        jobApplicationService.updateApplicationStatus(principal.getUser().getId(), jobId, appId, status);
        return "redirect:/employer/jobs/" + jobId + "/applications";
    }*/

    @PostMapping("/{jobId}/applications/{appId}/status")
    public String updateStatus(@AuthenticationPrincipal UserPrincipal principal,
                               @PathVariable Long jobId,
                               @PathVariable Long appId,
                               @RequestParam JobApplication.Status status,
                               RedirectAttributes ra) {
        try {
            jobApplicationService.updateApplicationStatus(principal.getUser().getId(), jobId, appId, status);
            ra.addFlashAttribute("success", "Applicant status updated to " + status + ".");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/employer/jobs/" + jobId + "/applications";
    }
}
