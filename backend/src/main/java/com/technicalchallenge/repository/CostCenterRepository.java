package com.technicalchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.CostCenter;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Long> {}
