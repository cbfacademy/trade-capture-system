package com.technicalchallenge.dto;

import lombok.Data;

@Data
public class UserPrivilegeDTO {//DTO for transferring user-privilege association data
    private Long userId;
    private Long privilegeId;
    // getters and setters
}
