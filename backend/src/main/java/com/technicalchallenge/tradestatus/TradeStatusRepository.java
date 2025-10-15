package com.technicalchallenge.tradestatus;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeStatusRepository extends JpaRepository<TradeStatus, Long> {
    Optional<TradeStatus> findByTradeStatus(String tradeStatus);
}
