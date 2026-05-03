package com.jobportal.findworks.service.impl;

import com.jobportal.findworks.entity.location.City;
import com.jobportal.findworks.repository.location.CityRepository;
import com.jobportal.findworks.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final CityRepository cityRepository;

    public List<City> listAllCities() {
        return cityRepository.findAllByOrderByNameAsc();
    }

    public City getCityOrThrow(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid city"));
    }
}