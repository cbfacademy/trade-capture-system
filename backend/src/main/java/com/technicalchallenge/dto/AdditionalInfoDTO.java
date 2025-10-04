package com.technicalchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfoDTO {//DTO for transferring additional information data

    private Long id; // Unique identifier for the additional info

    private String entityType; // Type of entity the additional info is associated with

    private Long entityId; // Identifier of the associated entity

    private String fieldName; // Name of the additional field

    private String fieldValue; // Value of the additional field

    private String fieldType; // Data type of the additional field (e.g., String, Integer)

    private Boolean active; // Indicates if the additional info is active

    private LocalDateTime createdDate; // Timestamp of when the additional info was created

    private LocalDateTime lastModifiedDate; // Timestamp of the last modification

    private Integer version; // Version number for optimistic locking
}
