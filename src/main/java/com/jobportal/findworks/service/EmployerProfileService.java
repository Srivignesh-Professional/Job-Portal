package com.jobportal.findworks.service;

import com.jobportal.findworks.dto.EmployerProfileForm;
import com.jobportal.findworks.entity.EmployerProfile;
import com.jobportal.findworks.entity.User;

import java.util.Optional;

public interface EmployerProfileService {

    Optional<EmployerProfile> findByUserId(Long userId);

    boolean profileExists(Long userId);

    /*EmployerProfile upsertProfile(User user, EmployerProfileForm form);*/
    EmployerProfile upsertProfile(Long userId, EmployerProfileForm form);
}
