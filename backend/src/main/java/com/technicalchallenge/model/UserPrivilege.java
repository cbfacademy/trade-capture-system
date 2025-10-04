package com.technicalchallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_privilege")
@IdClass(UserPrivilegeId.class)
public class UserPrivilege {// Entity to represent the association between User and Privilege
    @Id
    private Long userId;

    @Id
    private Long privilegeId;
}
