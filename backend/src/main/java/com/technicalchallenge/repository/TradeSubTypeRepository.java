package com.technicalchallenge.repository;

import com.technicalchallenge.model.TradeSubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing TradeSubType entities in the database
public interface TradeSubTypeRepository extends JpaRepository<TradeSubType, Long> {
    // Custom query methods
    Optional<TradeSubType> findByTradeSubType(String tradeSubType);
}
