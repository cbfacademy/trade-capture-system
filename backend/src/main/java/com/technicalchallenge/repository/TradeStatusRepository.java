package com.technicalchallenge.repository;

import com.technicalchallenge.model.TradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeStatusRepository extends JpaRepository<TradeStatus, Long> {
    Optional<TradeStatus> findByTradeStatus(String tradeStatus);
}
