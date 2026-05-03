package com.jobportal.findworks.controller;

import com.jobportal.findworks.dto.job.JobSearchForm;
import com.jobportal.findworks.service.CatalogService;
import com.jobportal.findworks.service.LocationService;
import com.jobportal.findworks.service.impl.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/jobs")
public class JobController {

    private final JobPostService jobPostService;
    private final LocationService locationService;
    private final CatalogService catalogService;

    @GetMapping("/jobs")
    public String listJobs(@ModelAttribute("search") JobSearchForm search, Model model) {
        model.addAttribute("cities", locationService.listAllCities());
        model.addAttribute("categories", catalogService.listActiveCategories());
        model.addAttribute("jobs", jobPostService.listPublished(search.getCityId(), search.getCategoryId()));
        return "jobs/list";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        model.addAttribute("job", jobPostService.getOrThrow(id));
        return "jobs/detail";
    }
}