package com.technicalchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.TradeLeg;

@Repository
public interface TradeLegRepository extends JpaRepository<TradeLeg, Long> {}
