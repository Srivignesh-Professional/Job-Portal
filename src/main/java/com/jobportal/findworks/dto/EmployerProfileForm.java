package com.jobportal.findworks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployerProfileForm {

    @NotBlank
    private String companyName;

    private String contactName;

    @NotNull
    private Long cityId;

    private String addressLine;
}