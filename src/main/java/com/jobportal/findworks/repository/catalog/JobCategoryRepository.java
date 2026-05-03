package com.jobportal.findworks.repository.catalog;

import com.jobportal.findworks.entity.catalog.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    List<JobCategory> findByActiveTrueOrderByNameAsc();
}