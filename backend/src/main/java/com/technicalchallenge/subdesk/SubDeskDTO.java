package com.technicalchallenge.subdesk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class SubDeskDTO {
    private Long id;
    private String subdeskName;
    private String deskName;
}
