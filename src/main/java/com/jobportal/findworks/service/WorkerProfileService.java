package com.jobportal.findworks.service;

import com.jobportal.findworks.dto.WorkerProfileForm;
import com.jobportal.findworks.entity.WorkerProfile;

import java.util.Optional;

public interface WorkerProfileService {

    WorkerProfile upsertProfile(Long userId, WorkerProfileForm form);

    Optional<WorkerProfile> findByUserId(Long userId);

    boolean profileExists(Long userId);
}
