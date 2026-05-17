package com.jobportal.findworks.dto;

import com.jobportal.findworks.entity.WorkerProfile;
import com.jobportal.findworks.entity.application.JobApplication;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicantView {
    private Long applicationId;
    private Long workerUserId;
    private String workerPhone;
    private String workerName;
    private WorkerProfile.Availability availability;
    private LocalDateTime appliedAt;
    private JobApplication.Status status;
}