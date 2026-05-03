package com.jobportal.findworks.service.impl;

import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.entity.application.JobApplication;
import com.jobportal.findworks.entity.job.JobPost;
import com.jobportal.findworks.repository.JobApplicationRepository;
import com.jobportal.findworks.repository.JobPostRepository;
import com.jobportal.findworks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostService jobPostService;
    private final UserRepository userRepository;

    public List<JobApplication> myApplications(Long workerUserId) {
        return jobApplicationRepository.findByWorkerIdOrderByAppliedAtDesc(workerUserId);
    }

    @Transactional
    public void apply(Long workerUserId, Long jobId) {
        JobPost job = jobPostService.getOrThrow(jobId);

        if (job.getStatus() != JobPost.Status.PUBLISHED) {
            throw new IllegalArgumentException("Job is closed");
        }

        if (jobApplicationRepository.existsByJobPostIdAndWorkerId(jobId, workerUserId)) {
            throw new IllegalArgumentException("You already applied for this job");
        }

        User worker = userRepository.getReferenceById(workerUserId);

        JobApplication app = new JobApplication();
        app.setJobPost(job);
        app.setWorker(worker);
        app.setStatus(JobApplication.Status.APPLIED);
        app.setUpdatedAt(LocalDateTime.now());

        jobApplicationRepository.save(app);
    }
}