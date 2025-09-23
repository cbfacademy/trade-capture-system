package com.technicalchallenge.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
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
