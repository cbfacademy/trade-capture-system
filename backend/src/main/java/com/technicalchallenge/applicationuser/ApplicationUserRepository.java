package com.technicalchallenge.applicationuser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    // Custom query methods if needed
    Optional<ApplicationUser> findByLoginId(String loginId);
    Optional<ApplicationUser> findByFirstName(String firstName);
}
