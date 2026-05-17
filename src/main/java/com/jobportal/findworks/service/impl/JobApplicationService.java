package com.jobportal.findworks.service.impl;

import com.jobportal.findworks.dto.ApplicantView;
import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.entity.WorkerProfile;
import com.jobportal.findworks.entity.application.JobApplication;
import com.jobportal.findworks.entity.job.JobPost;
import com.jobportal.findworks.repository.JobApplicationRepository;
import com.jobportal.findworks.repository.UserRepository;
import com.jobportal.findworks.repository.WorkerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobPostService jobPostService;
    private final UserRepository userRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final WorkerProfileRepository workerProfileRepository;

    public List<JobApplication> myApplications(Long workerUserId) {
        return jobApplicationRepository.findByWorker_IdOrderByAppliedAtDesc(workerUserId);
    }

    @Transactional
    public void apply(Long workerUserId, Long jobId) {
        JobPost job = jobPostService.getOrThrow(jobId);

        if (job.getStatus() != JobPost.Status.PUBLISHED) {
            throw new IllegalArgumentException("Job is closed");
        }

        if (jobApplicationRepository.existsByJobPost_IdAndWorker_Id(jobId, workerUserId)) {
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

    @Transactional(readOnly = true)
    /*public List<ApplicantView> listApplicantsForEmployer(Long employerUserId, Long jobId) {
        JobPost job = jobPostService.getOrThrow(jobId);

        // security: employer can only view their own job applicants
        if (!job.getEmployer().getId().equals(employerUserId)) {
            throw new IllegalArgumentException("Not allowed");
        }

        // Use the correct method based on your entity model:
        List<JobApplication> apps = jobApplicationRepository.findByJobPost_IdOrderByAppliedAtDesc(jobId);
        // If you use relationships, replace above with:
        // List<JobApplication> apps = jobApplicationRepository.findByJobPost_IdOrderByAppliedAtDesc(jobId);

        List<Long> workerIds = apps.stream()
                .map(a -> a.getWorker().getId()) // if you have workerUserId field
                .distinct()
                .toList();

        // phone from users table
        Map<Long, String> phoneByUserId = userRepository.findAllById(workerIds).stream()
                .collect(Collectors.toMap(u -> u.getId(), u -> u.getPhone()));

        // name/availability from worker_profile (if exists)
        Map<Long, WorkerProfile> profileByUserId =
                workerProfileRepository.findAllById(workerIds).stream()
                        .collect(Collectors.toMap(p -> p.getUserId(), Function.identity()));

        List<ApplicantView> result = new ArrayList<>();
        for (JobApplication a : apps) {
            Long workerId = a.getWorker().getId(); // if relationship, then a.getWorker().getId()

            var profile = profileByUserId.get(workerId);

            result.add(ApplicantView.builder()
                    .applicationId(a.getId())
                    .workerUserId(workerId)
                    .workerPhone(phoneByUserId.get(workerId))
                    .workerName(profile != null ? profile.getFullName() : null)
                    .availability(profile != null ? profile.getAvailability() : null)
                    .appliedAt(a.getAppliedAt())
                    .status(a.getStatus())
                    .build());
        }

        return result;
    }*/
    public List<ApplicantView> listApplicantsForEmployer(Long employerUserId, Long jobId) {
        JobPost job = jobPostService.getOrThrow(jobId);

        // Security check
        if (!job.getEmployer().getId().equals(employerUserId)) {
            throw new IllegalArgumentException("Unauthorized access to this job");
        }

        // Note the underscore for nested property: JobPost -> Id
        List<JobApplication> apps = jobApplicationRepository.findByJobPost_IdOrderByAppliedAtDesc(jobId);

        // Use .getWorker().getId() instead of the missing getWorkerUserId()
        List<Long> workerIds = apps.stream()
                .map(a -> a.getWorker().getId())
                .distinct()
                .toList();

        Map<Long, String> phoneByUserId = userRepository.findAllById(workerIds).stream()
                .collect(Collectors.toMap(u -> u.getId(), u -> u.getPhone()));

        Map<Long, WorkerProfile> profileByUserId =
                workerProfileRepository.findAllById(workerIds).stream()
                        .collect(Collectors.toMap(p -> p.getUserId(), java.util.function.Function.identity()));

        List<ApplicantView> result = new ArrayList<>();
        for (JobApplication a : apps) {
            Long workerId = a.getWorker().getId(); // Corrected way to get ID
            var profile = profileByUserId.get(workerId);

            result.add(ApplicantView.builder()
                    .applicationId(a.getId())
                    .workerUserId(workerId)
                    .workerPhone(phoneByUserId.get(workerId))
                    .workerName(profile != null ? profile.getFullName() : "Name Not Set")
                    .availability(profile != null ? profile.getAvailability() : null)
                    .appliedAt(a.getAppliedAt())
                    .status(a.getStatus())
                    .build());
        }
        return result;
    }

    @Transactional
    public void updateApplicationStatus(Long employerUserId, Long jobId, Long applicationId, JobApplication.Status status) {
        JobPost job = jobPostService.getOrThrow(jobId);

        if (!job.getEmployer().getId().equals(employerUserId)) {
            throw new IllegalArgumentException("Not allowed");
        }

        JobApplication app = jobApplicationRepository.findByIdAndJobPost_Id(applicationId, jobId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        app.setStatus(status);
        app.setUpdatedAt(LocalDateTime.now());
        jobApplicationRepository.save(app);
    }
}