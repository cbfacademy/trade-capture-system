package com.technicalchallenge.repository;

import com.technicalchallenge.model.BusinessDayConvention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessDayConventionRepository extends JpaRepository<BusinessDayConvention, Long> {
    Optional<BusinessDayConvention> findByBdc(String bdc);
}
