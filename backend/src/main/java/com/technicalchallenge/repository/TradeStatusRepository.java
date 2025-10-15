package com.technicalchallenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.TradeStatus;

@Repository
public interface TradeStatusRepository extends JpaRepository<TradeStatus, Long> {
    Optional<TradeStatus> findByTradeStatus(String tradeStatus);
}
