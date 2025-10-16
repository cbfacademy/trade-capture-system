package com.technicalchallenge.cashflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashflowRepository extends JpaRepository<Cashflow, Long> {
    // Custom query methods if needed
}
