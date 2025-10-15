package com.technicalchallenge.tradeleg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeLegRepository extends JpaRepository<TradeLeg, Long> {}
