package com.technicalchallenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.BusinessDayConvention;

@Repository
public interface BusinessDayConventionRepository extends JpaRepository<BusinessDayConvention, Long> {
    Optional<BusinessDayConvention> findByBdc(String bdc);
}
