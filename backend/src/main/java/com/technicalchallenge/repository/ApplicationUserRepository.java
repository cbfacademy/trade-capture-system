package com.technicalchallenge.repository;

import com.technicalchallenge.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing ApplicationUser entities in the database
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    // Custom query methods if needed
    Optional<ApplicationUser> findByLoginId(String loginId);
    Optional<ApplicationUser> findByFirstName(String firstName);
}
