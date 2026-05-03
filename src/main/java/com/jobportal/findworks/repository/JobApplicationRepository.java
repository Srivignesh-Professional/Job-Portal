package com.jobportal.findworks.repository;

import com.jobportal.findworks.entity.application.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobPostIdAndWorkerId(Long jobPostId, Long workerId);
    List<JobApplication> findByWorkerIdOrderByAppliedAtDesc(Long workerId);
}