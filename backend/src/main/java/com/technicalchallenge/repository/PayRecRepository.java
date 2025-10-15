package com.technicalchallenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.PayRec;

@Repository
public interface PayRecRepository extends JpaRepository<PayRec, Long> {
    Optional<PayRec> findByPayRec(String payRec);
}
