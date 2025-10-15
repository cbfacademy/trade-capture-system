package com.technicalchallenge.tradetype;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeTypeRepository extends JpaRepository<TradeType, Long> {
    // Custom query methods
    Optional<TradeType> findByTradeType(String tradeType);
}
