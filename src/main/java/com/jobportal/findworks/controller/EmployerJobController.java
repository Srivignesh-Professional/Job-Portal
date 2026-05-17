package com.jobportal.findworks.controller;

import com.jobportal.findworks.dto.job.JobPostForm;
import com.jobportal.findworks.entity.job.JobPost;
import com.jobportal.findworks.repository.JobApplicationRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/jobs")
public class EmployerJobController {

    private final JobPostService jobPostService;
    private final LocationService locationService;
    private final CatalogService catalogService;
    private final JobApplicationRepository jobApplicationRepository;

    /*@GetMapping
    public String myJobs(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("jobs", jobPostService.listEmployerJobs(principal.getUser().getId()));
        return "jobs/employer-my";
    }*/

    @GetMapping
    public String myJobs(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        Long employerId = principal.getUser().getId();
        List<JobPost> jobs = jobPostService.listEmployerJobs(employerId);

        model.addAttribute("jobs", jobs);

        List<Long> jobIds = jobs.stream().map(JobPost::getId).toList();
        Map<Long, Long> applicantCounts = new HashMap<>();

        if (!jobIds.isEmpty()) {
            for (var row : jobApplicationRepository.countApplicantsByJobIds(jobIds)) {
                applicantCounts.put(row.getJobId(), row.getCount());
            }
        }

        model.addAttribute("applicantCounts", applicantCounts);
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
                         Model model,
                         RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", locationService.listAllCities());
            model.addAttribute("categories", catalogService.listActiveCategories());
            model.addAttribute("wageTypes", JobPost.WageType.values());
            return "jobs/create";
        }

        jobPostService.createJob(principal.getUser().getId(), form);
        ra.addFlashAttribute("success", "Job posted successfully.");
        return "redirect:/employer/jobs";
    }


    @PostMapping("/{id}/close")
    public String close(@AuthenticationPrincipal UserPrincipal principal,
                        @PathVariable Long id,
                        RedirectAttributes ra) {
        try {
            jobPostService.closeJob(principal.getUser().getId(), id);
            ra.addFlashAttribute("success", "Job closed successfully.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/employer/jobs";
    }


}