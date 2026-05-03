package com.jobportal.findworks.service.impl;

import com.jobportal.findworks.entity.catalog.JobCategory;
import com.jobportal.findworks.entity.location.City;
import com.jobportal.findworks.repository.catalog.JobCategoryRepository;
import com.jobportal.findworks.repository.location.CityRepository;
import com.jobportal.findworks.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final JobCategoryRepository jobCategoryRepository;

    public List<JobCategory> listActiveCategories() {
        return jobCategoryRepository.findByActiveTrueOrderByNameAsc();
    }

    public JobCategory getCategoryOrThrow(Long id) {
        return jobCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));
    }
}
