package com.technicalchallenge.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.repository.TradeLegRepository;

@Service
public class TradeLegService {
    private static final Logger logger = LoggerFactory.getLogger(TradeLegService.class);

    @Autowired
    private TradeLegRepository tradeLegRepository;

    public List<TradeLeg> getAllTradeLegs() {
        logger.info("Retrieving all trade legs");
        return tradeLegRepository.findAll();
    }

    public Optional<TradeLeg> getTradeLegById(Long id) {
        logger.debug("Retrieving trade leg by id: {}", id);
        return tradeLegRepository.findById(id);
    }

    public TradeLeg saveTradeLeg(TradeLeg tradeLeg, TradeLegDTO dto) {
        logger.info("Saving trade leg: {}", tradeLeg);
        // Ensure TradeLeg is saved with related entities set, not just IDs
        return tradeLegRepository.save(tradeLeg);
    }

    public void deleteTradeLeg(Long id) {
        logger.warn("Deleting trade leg with id: {}", id);
        tradeLegRepository.deleteById(id);
    }

    // Business logic: notional > 0, trade, currency, legRateType required (enforced in controller)
}
