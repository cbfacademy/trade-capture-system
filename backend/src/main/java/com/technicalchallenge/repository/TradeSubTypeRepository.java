package com.technicalchallenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.TradeSubType;

@Repository
public interface TradeSubTypeRepository extends JpaRepository<TradeSubType, Long> {
    // Custom query methods
    Optional<TradeSubType> findByTradeSubType(String tradeSubType);
}
