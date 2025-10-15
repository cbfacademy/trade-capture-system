package com.technicalchallenge.applicationuser;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String loginId;
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean active;
    private int version;
    private LocalDateTime lastModifiedTimestamp;
    private String userProfile;
}
