package com.technicalchallenge.repository;

import com.technicalchallenge.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Repository interface for managing UserProfile entities in the database
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserType(String userType);
}

