package com.technicalchallenge.repository;

import com.technicalchallenge.model.UserPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repository interface for managing UserPrivilege entities in the database
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, Long> {}
