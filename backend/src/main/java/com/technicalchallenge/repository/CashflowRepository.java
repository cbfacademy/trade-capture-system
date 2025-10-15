package com.technicalchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.Cashflow;

@Repository
public interface CashflowRepository extends JpaRepository<Cashflow, Long> {
    // Custom query methods if needed
}
