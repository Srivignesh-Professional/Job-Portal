package com.jobportal.findworks.repository;

import com.jobportal.findworks.entity.job.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    List<JobPost> findByEmployerIdOrderByCreatedAtDesc(Long employerId);

    @Query("""
        select j from JobPost j
        where j.status = 'PUBLISHED'
          and (:cityId is null or j.city.id = :cityId)
          and (:categoryId is null or j.category.id = :categoryId)
        order by j.createdAt desc
    """)
    List<JobPost> searchPublished(Long cityId, Long categoryId);
}