package com.jobportal.findworks.service.impl;

import com.jobportal.findworks.dto.EmployerProfileForm;
import com.jobportal.findworks.entity.EmployerProfile;
import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.repository.EmployerProfileRepository;
import com.jobportal.findworks.repository.UserRepository;
import com.jobportal.findworks.service.EmployerProfileService;
import com.jobportal.findworks.service.LocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerProfileServiceImpl implements EmployerProfileService {

    /*private final EmployerProfileRepository employerProfileRepository;
    private final LocationService locationService;

    public Optional<EmployerProfile> findByUserId(Long userId) {
        return employerProfileRepository.findById(userId);
    }

    public boolean profileExists(Long userId) {
        return employerProfileRepository.existsById(userId);
    }

    public EmployerProfile upsertProfile(User user, EmployerProfileForm form) {
        EmployerProfile profile = employerProfileRepository.findById(user.getId())
                .orElseGet(EmployerProfile::new);

        profile.setUser(user);
        profile.setCompanyName(form.getCompanyName());
        profile.setContactName(form.getContactName());
        profile.setCity(locationService.getCityOrThrow(form.getCityId()));
        profile.setAddressLine(form.getAddressLine());
        profile.setUpdatedAt(LocalDateTime.now());

        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(LocalDateTime.now());
        }

        return employerProfileRepository.save(profile);
    }*/
    private final EmployerProfileRepository employerProfileRepository;
    private final LocationService locationService;
    private final UserRepository userRepository; // Inject UserRepository

    public Optional<EmployerProfile> findByUserId(Long userId) {
        return employerProfileRepository.findById(userId);
    }

    public boolean profileExists(Long userId) {
        return employerProfileRepository.existsById(userId);
    }

    @Transactional
    @Override
    public EmployerProfile upsertProfile(Long userId, EmployerProfileForm form) {
        // Get a managed reference (avoids detached entity error)
        User managedUser = userRepository.getReferenceById(userId);

        EmployerProfile profile = employerProfileRepository.findById(userId)
                .orElseGet(EmployerProfile::new);

        // Set the managed user
        profile.setUser(managedUser);

        profile.setCompanyName(form.getCompanyName());
        profile.setContactName(form.getContactName());
        profile.setCity(locationService.getCityOrThrow(form.getCityId()));
        profile.setAddressLine(form.getAddressLine());
        profile.setUpdatedAt(LocalDateTime.now());

        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(LocalDateTime.now());
        }

        return employerProfileRepository.save(profile);
    }
}