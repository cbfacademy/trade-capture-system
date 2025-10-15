package com.technicalchallenge.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "application_user")
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String loginId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
    private int version;
    private LocalDateTime lastModifiedTimestamp;

    @PrePersist
    public void prePersist() {
        this.lastModifiedTimestamp = java.time.LocalDateTime.now();
        this.version = 1;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedTimestamp = java.time.LocalDateTime.now();
        this.version = this.version + 1;
    }
}
