package com.jobportal.findworks.service.impl;

import com.jobportal.findworks.dto.job.JobPostForm;
import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.entity.job.JobPost;
import com.jobportal.findworks.repository.JobPostRepository;
import com.jobportal.findworks.repository.UserRepository;
import com.jobportal.findworks.service.CatalogService;
import com.jobportal.findworks.service.LocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final CatalogService catalogService;
    private final LocationService locationService;

    public List<JobPost> listPublished(Long cityId, Long categoryId) {
        return jobPostRepository.searchPublished(cityId, categoryId);
    }

    public JobPost getOrThrow(Long id) {
        return jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
    }

    public List<JobPost> listEmployerJobs(Long employerUserId) {
        return jobPostRepository.findByEmployerIdOrderByCreatedAtDesc(employerUserId);
    }

    @Transactional
    public JobPost createJob(Long employerUserId, JobPostForm form) {
        User employer = userRepository.getReferenceById(employerUserId);

        JobPost job = new JobPost();
        job.setEmployer(employer);
        job.setTitle(form.getTitle());
        job.setDescription(form.getDescription());
        job.setWageType(form.getWageType());
        job.setWageAmount(form.getWageAmount());
        job.setWorkDate(form.getWorkDate());
        job.setCategory(catalogService.getCategoryOrThrow(form.getCategoryId()));
        job.setCity(locationService.getCityOrThrow(form.getCityId()));
        job.setStatus(JobPost.Status.PUBLISHED);
        job.setUpdatedAt(LocalDateTime.now());

        return jobPostRepository.save(job);
    }

    @Transactional
    public void closeJob(Long employerUserId, Long jobId) {
        JobPost job = getOrThrow(jobId);

        if (!job.getEmployer().getId().equals(employerUserId)) {
            throw new IllegalArgumentException("Not allowed to close this job");
        }

        job.setStatus(JobPost.Status.CLOSED);
        job.setUpdatedAt(LocalDateTime.now());
        jobPostRepository.save(job);
    }
}
