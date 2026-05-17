package com.jobportal.findworks.controller;

import com.jobportal.findworks.dto.job.JobSearchForm;
import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.entity.job.JobPost;
import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.CatalogService;
import com.jobportal.findworks.service.LocationService;
import com.jobportal.findworks.service.WorkerProfileService;
import com.jobportal.findworks.service.impl.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/jobs")
public class JobController {

    private final JobPostService jobPostService;
    private final LocationService locationService;
    private final CatalogService catalogService;
    private final WorkerProfileService workerProfileService;

    /*@GetMapping("/jobs")
    public String listJobs(@ModelAttribute("search") JobSearchForm search, Model model) {
        model.addAttribute("cities", locationService.listAllCities());
        model.addAttribute("categories", catalogService.listActiveCategories());
        model.addAttribute("jobs", jobPostService.listPublished(search.getCityId(), search.getCategoryId()));
        return "jobs/list";
    }*/

    @GetMapping("/jobs/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        model.addAttribute("job", jobPostService.getOrThrow(id));
        return "jobs/detail";
    }

    /*@GetMapping("/jobs")
    public String listJobs(@ModelAttribute("search") JobSearchForm search,
                           @AuthenticationPrincipal UserPrincipal principal,
                           Model model) {

        model.addAttribute("cities", locationService.listAllCities());
        model.addAttribute("categories", catalogService.listActiveCategories());

        // Default behaviour
        List<JobPost> jobs;

        boolean cityFilterChosen = (search.getCityId() != null);

        if (!cityFilterChosen
                && principal != null
                && principal.getUser().getRole() == User.Role.WORKER) {

            var profileOpt = workerProfileService.findByUserId(principal.getUser().getId());

            var preferredCityIds = profileOpt
                    .map(p -> p.getPreferredCities().stream().map(c -> c.getId()).toList())
                    .orElse(List.of());

            if (!preferredCityIds.isEmpty()) {
                jobs = jobPostService.listPublishedForCities(preferredCityIds, search.getCategoryId());
                model.addAttribute("info", "Showing jobs in your preferred cities. Use filters to change.");
            } else {
                jobs = jobPostService.listPublished(null, search.getCategoryId());
            }

        } else {
            // Normal filter flow (all users)
            jobs = jobPostService.listPublished(search.getCityId(), search.getCategoryId());
        }

        model.addAttribute("jobs", jobs);
        return "jobs/list";
    }*/


    @GetMapping("/jobs")
    public String listJobs(@ModelAttribute("search") JobSearchForm search,
                           @AuthenticationPrincipal UserPrincipal principal,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model) {

        model.addAttribute("cities", locationService.listAllCities());
        model.addAttribute("categories", catalogService.listActiveCategories());

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<JobPost> jobsPage;

        boolean cityFilterChosen = (search.getCityId() != null);

        if (!cityFilterChosen
                && principal != null
                && principal.getUser().getRole() == User.Role.WORKER) {

            var profileOpt = workerProfileService.findByUserId(principal.getUser().getId());
            var preferredCityIds = profileOpt
                    .map(p -> p.getPreferredCities().stream().map(c -> c.getId()).toList())
                    .orElse(java.util.List.of());

            if (!preferredCityIds.isEmpty()) {
                jobsPage = jobPostService.listPublishedForCitiesPage(preferredCityIds, search.getCategoryId(), pageable);
                model.addAttribute("info", "Showing jobs in your preferred cities. Use filters to change.");
            } else {
                jobsPage = jobPostService.listPublishedPage(null, search.getCategoryId(), pageable);
            }
        } else {
            jobsPage = jobPostService.listPublishedPage(search.getCityId(), search.getCategoryId(), pageable);
        }

        model.addAttribute("jobsPage", jobsPage);
        model.addAttribute("pageSize", size);

        return "jobs/list";
    }
}