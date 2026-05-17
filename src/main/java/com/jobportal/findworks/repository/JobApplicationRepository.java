package com.jobportal.findworks.repository;

import com.jobportal.findworks.entity.application.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobPost_IdAndWorker_Id(Long jobPostId, Long workerId);

    List<JobApplication> findByWorker_IdOrderByAppliedAtDesc(Long workerId);

    List<JobApplication> findByJobPost_IdOrderByAppliedAtDesc(Long jobPostId);

    Optional<JobApplication> findByIdAndJobPost_Id(Long id, Long jobPostId);

    interface JobApplicantCount {
        Long getJobId();
        Long getCount();
    }

    @Query("""
        select a.jobPost.id as jobId, count(a.id) as count
        from JobApplication a
        where a.jobPost.id in :jobIds
        group by a.jobPost.id
    """)
    List<JobApplicantCount> countApplicantsByJobIds(@Param("jobIds") List<Long> jobIds);

}