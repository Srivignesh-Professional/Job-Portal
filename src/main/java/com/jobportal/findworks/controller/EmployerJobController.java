package com.jobportal.findworks.controller;

import com.jobportal.findworks.dto.job.JobPostForm;
import com.jobportal.findworks.entity.job.JobPost;
import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.CatalogService;
import com.jobportal.findworks.service.LocationService;
import com.jobportal.findworks.service.impl.JobPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/jobs")
public class EmployerJobController {

    private final JobPostService jobPostService;
    private final LocationService locationService;
    private final CatalogService catalogService;

    @GetMapping
    public String myJobs(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("jobs", jobPostService.listEmployerJobs(principal.getUser().getId()));
        return "jobs/employer-my";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new JobPostForm());
        model.addAttribute("cities", locationService.listAllCities());
        model.addAttribute("categories", catalogService.listActiveCategories());
        model.addAttribute("wageTypes", JobPost.WageType.values());
        return "jobs/create";
    }

    @PostMapping
    public String create(@AuthenticationPrincipal UserPrincipal principal,
                         @Valid @ModelAttribute("form") JobPostForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", locationService.listAllCities());
            model.addAttribute("categories", catalogService.listActiveCategories());
            model.addAttribute("wageTypes", JobPost.WageType.values());
            return "jobs/create";
        }

        jobPostService.createJob(principal.getUser().getId(), form);
        return "redirect:/employer/jobs";
    }

    @PostMapping("/{id}/close")
    public String close(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        jobPostService.closeJob(principal.getUser().getId(), id);
        return "redirect:/employer/jobs";
    }
}