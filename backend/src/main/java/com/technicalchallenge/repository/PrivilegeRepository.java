package com.technicalchallenge.repository;

import com.technicalchallenge.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repository interface for managing Privilege entities in the database
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {}
