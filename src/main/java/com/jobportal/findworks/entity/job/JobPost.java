package com.jobportal.findworks.entity.job;


import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.entity.catalog.JobCategory;
import com.jobportal.findworks.entity.location.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "job_post")
public class JobPost {

    public enum WageType { DAILY, HOURLY, MONTHLY, FIXED }
    public enum Status { PUBLISHED, CLOSED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_user_id", nullable = false)
    private User employer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private JobCategory category;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "wage_type", nullable = false, length = 20)
    private WageType wageType;

    @Column(name = "wage_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal wageAmount;

    @Column(name = "work_date")
    private LocalDate workDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.PUBLISHED;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}