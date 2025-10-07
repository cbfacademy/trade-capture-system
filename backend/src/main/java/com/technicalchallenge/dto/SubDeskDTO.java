package com.technicalchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class SubDeskDTO {//DTO for transferring sub-desk data
    private Long id;
    private String subdeskName;
    private String deskName;
}
