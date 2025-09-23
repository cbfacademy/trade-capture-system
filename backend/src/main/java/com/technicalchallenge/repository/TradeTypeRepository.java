package com.technicalchallenge.repository;

import com.technicalchallenge.model.TradeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeTypeRepository extends JpaRepository<TradeType, Long> {
    // Custom query methods
    Optional<TradeType> findByTradeType(String tradeType);
}
