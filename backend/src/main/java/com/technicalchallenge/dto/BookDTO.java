package com.technicalchallenge.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO {//DTO for transferring book data
    private Long id;
    private String bookName;
    private boolean active;
    private int version;
    private String costCenterName;
}
