package com.technicalchallenge.repository;

import com.technicalchallenge.model.PayRec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayRecRepository extends JpaRepository<PayRec, Long> {
    Optional<PayRec> findByPayRec(String payRec);
}
