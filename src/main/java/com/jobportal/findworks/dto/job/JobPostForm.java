package com.jobportal.findworks.dto.job;

import com.jobportal.findworks.entity.job.JobPost;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class JobPostForm {

    @NotBlank
    private String title;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long cityId;

    private String description;

    @NotNull
    private JobPost.WageType wageType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal wageAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate workDate;
}