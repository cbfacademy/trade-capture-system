package com.technicalchallenge.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CounterpartyDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private Long internalCode;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
    private boolean active;
}
