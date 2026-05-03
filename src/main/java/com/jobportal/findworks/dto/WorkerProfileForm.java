package com.jobportal.findworks.dto;


import com.jobportal.findworks.entity.WorkerProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WorkerProfileForm {

    @NotBlank
    private String fullName;

    @NotNull
    private List<Long> preferredCityIds = new ArrayList<>();

    @NotNull
    private WorkerProfile.Availability availability;
}