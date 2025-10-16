package com.technicalchallenge.payrec;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRecRepository extends JpaRepository<PayRec, Long> {
    Optional<PayRec> findByPayRec(String payRec);
}
