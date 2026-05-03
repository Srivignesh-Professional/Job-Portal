package com.jobportal.findworks.entity;

import com.jobportal.findworks.entity.location.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "worker_profile")
public class WorkerProfile {

    public enum Availability { FULL_TIME, PART_TIME, ON_CALL }

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name", nullable = false, length = 120)
    private String fullName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "worker_preferred_city",
            joinColumns = @JoinColumn(name = "worker_user_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private Set<City> preferredCities = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", length = 20)
    private Availability availability;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}