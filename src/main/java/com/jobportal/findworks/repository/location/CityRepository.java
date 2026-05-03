package com.jobportal.findworks.repository.location;

import com.jobportal.findworks.entity.location.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findAllByOrderByNameAsc();
}