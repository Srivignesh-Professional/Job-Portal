package com.jobportal.findworks.service;

import com.jobportal.findworks.entity.location.City;

import java.util.List;

public interface LocationService {

    List<City> listAllCities();

    City getCityOrThrow(Long cityId);
}
