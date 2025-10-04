package com.technicalchallenge.repository;

import com.technicalchallenge.model.TradeLeg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repository interface for managing TradeLeg entities in the database
public interface TradeLegRepository extends JpaRepository<TradeLeg, Long> {}
