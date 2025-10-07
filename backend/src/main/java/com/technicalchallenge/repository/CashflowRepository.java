package com.technicalchallenge.repository;

import com.technicalchallenge.model.Cashflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repository interface for managing Cashflow entities in the database
public interface CashflowRepository extends JpaRepository<Cashflow, Long> {
    // Custom query methods if needed
}
