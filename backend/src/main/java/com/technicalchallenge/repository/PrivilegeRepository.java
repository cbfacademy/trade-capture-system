package com.technicalchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {}
