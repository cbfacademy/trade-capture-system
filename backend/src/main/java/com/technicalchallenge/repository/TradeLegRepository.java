package com.technicalchallenge.repository;

import com.technicalchallenge.model.TradeLeg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeLegRepository extends JpaRepository<TradeLeg, Long> {}
