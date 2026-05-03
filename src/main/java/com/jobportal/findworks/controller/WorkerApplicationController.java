package com.jobportal.findworks.controller;

import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.impl.JobApplicationService;
import com.jobportal.findworks.service.impl.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/worker/applications")
public class WorkerApplicationController {

    private final JobApplicationService applicationService;
    private final JobPostService jobPostService;

//    @GetMapping
//    public String myApplications(@AuthenticationPrincipal UserPrincipal principal, Model model) {
//        model.addAttribute("applications", applicationService.findMyApplications(principal.getUser().getId()));
//        return "worker/applications/list";
//    }

    @GetMapping("/job/{jobId}/apply")
    public String apply(@PathVariable Long jobId, @AuthenticationPrincipal UserPrincipal principal) {
        applicationService.apply(jobId, principal.getUser().getId());
        return "redirect:/jobs/" + jobId;
    }
}
