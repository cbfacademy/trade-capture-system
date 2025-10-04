package com.technicalchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class DeskDTO {//DTO for transferring desk data
    private Long id;
    private String deskName;
}
