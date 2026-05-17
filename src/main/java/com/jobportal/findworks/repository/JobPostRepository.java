package com.jobportal.findworks.repository;

import com.jobportal.findworks.entity.job.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
        select j from JobPost j
        where j.status = 'PUBLISHED'
          and j.city.id in :cityIds
          and (:categoryId is null or j.category.id = :categoryId)
        order by j.createdAt desc
    """)
    List<JobPost> searchPublishedInCities(List<Long> cityIds, Long categoryId);

    @Query("""
        select j from JobPost j
        where j.status = 'PUBLISHED'
          and (:cityId is null or j.city.id = :cityId)
          and (:categoryId is null or j.category.id = :categoryId)
        order by j.createdAt desc
    """)
    Page<JobPost> searchPublishedPage(@Param("cityId") Long cityId,
                                      @Param("categoryId") Long categoryId,
                                      Pageable pageable);

    @Query("""
        select j from JobPost j
        where j.status = 'PUBLISHED'
          and j.city.id in :cityIds
          and (:categoryId is null or j.category.id = :categoryId)
        order by j.createdAt desc
    """)
    Page<JobPost> searchPublishedInCitiesPage(@Param("cityIds") java.util.List<Long> cityIds,
                                              @Param("categoryId") Long categoryId,
                                              Pageable pageable);
}