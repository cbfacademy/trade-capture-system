package com.technicalchallenge.repository;

import com.technicalchallenge.model.TradeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing TradeType entities in the database
public interface TradeTypeRepository extends JpaRepository<TradeType, Long> {
    // Custom query methods
    Optional<TradeType> findByTradeType(String tradeType);
}
