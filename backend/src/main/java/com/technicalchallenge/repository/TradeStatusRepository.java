package com.technicalchallenge.repository;

import com.technicalchallenge.model.TradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing TradeStatus entities in the database
public interface TradeStatusRepository extends JpaRepository<TradeStatus, Long> {
    Optional<TradeStatus> findByTradeStatus(String tradeStatus);
}
