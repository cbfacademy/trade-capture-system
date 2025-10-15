package com.technicalchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.UserPrivilege;

@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, Long> {}
