package com.jobportal.findworks.service;

import com.jobportal.findworks.entity.catalog.JobCategory;
import com.jobportal.findworks.entity.location.City;

import java.util.List;

public interface CatalogService {

    List<JobCategory> listActiveCategories();

    JobCategory getCategoryOrThrow(Long id);
}
