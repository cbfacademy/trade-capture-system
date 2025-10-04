package com.technicalchallenge.repository;

import com.technicalchallenge.model.PayRec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing PayRec entities in the database
public interface PayRecRepository extends JpaRepository<PayRec, Long> {
    Optional<PayRec> findByPayRec(String payRec);
}
