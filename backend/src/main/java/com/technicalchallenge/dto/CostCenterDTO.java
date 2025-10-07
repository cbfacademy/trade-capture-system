package com.technicalchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class CostCenterDTO {//DTO for transferring cost center data
    private Long id;
    private String costCenterName;
    private String subDeskName;
}
