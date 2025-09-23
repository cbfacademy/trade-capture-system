package com.technicalchallenge.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO {
    private Long id;
    private String bookName;
    private boolean active;
    private int version;
    private String costCenterName;
}
