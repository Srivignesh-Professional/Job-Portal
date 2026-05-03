package com.jobportal.findworks.repository;

import com.jobportal.findworks.entity.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, Long> {
}
