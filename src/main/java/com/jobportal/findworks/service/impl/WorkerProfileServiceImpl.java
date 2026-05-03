package com.jobportal.findworks.service.impl;

import com.jobportal.findworks.dto.WorkerProfileForm;
import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.entity.WorkerProfile;
import com.jobportal.findworks.entity.location.City;
import com.jobportal.findworks.repository.UserRepository;
import com.jobportal.findworks.repository.WorkerProfileRepository;
import com.jobportal.findworks.service.LocationService;
import com.jobportal.findworks.service.WorkerProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WorkerProfileServiceImpl implements WorkerProfileService {

    private final WorkerProfileRepository workerProfileRepository;
    private final UserRepository userRepository;
    private final LocationService locationService;

    public Optional<WorkerProfile> findByUserId(Long userId) {
        return workerProfileRepository.findById(userId);
    }

    public boolean profileExists(Long userId) {
        return workerProfileRepository.existsById(userId);
    }

    @Transactional
    public WorkerProfile upsertProfile(Long userId, WorkerProfileForm form) {
        User managedUser = userRepository.getReferenceById(userId);

        WorkerProfile profile = workerProfileRepository.findById(userId)
                .orElseGet(WorkerProfile::new);

        profile.setUser(managedUser);
        profile.setFullName(form.getFullName());
        profile.setAvailability(form.getAvailability());

        // Handle multiple cities
        Set<City> cities = new HashSet<>();
        if (form.getPreferredCityIds() != null) {
            for (Long cityId : form.getPreferredCityIds()) {
                cities.add(locationService.getCityOrThrow(cityId));
            }
        }
        profile.setPreferredCities(cities);

        profile.setUpdatedAt(LocalDateTime.now());

        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(LocalDateTime.now());
        }

        return workerProfileRepository.save(profile);
    }
}