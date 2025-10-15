package com.technicalchallenge.userprivilege;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, UserPrivilegeId> {
    List<UserPrivilege> findByUserId(Long userId);
}
