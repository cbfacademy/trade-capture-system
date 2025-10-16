package com.technicalchallenge.businessdayconvention;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessDayConventionRepository extends JpaRepository<BusinessDayConvention, Long> {
    Optional<BusinessDayConvention> findByBdc(String bdc);
}
