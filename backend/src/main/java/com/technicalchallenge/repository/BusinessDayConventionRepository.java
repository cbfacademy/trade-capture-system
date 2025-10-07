package com.technicalchallenge.repository;

import com.technicalchallenge.model.BusinessDayConvention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing BusinessDayConvention entities in the database
public interface BusinessDayConventionRepository extends JpaRepository<BusinessDayConvention, Long> {
    Optional<BusinessDayConvention> findByBdc(String bdc);
}
