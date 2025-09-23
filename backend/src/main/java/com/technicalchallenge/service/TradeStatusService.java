package com.technicalchallenge.service;

import com.technicalchallenge.model.TradeStatus;
import com.technicalchallenge.repository.TradeStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class TradeStatusService {
    private static final Logger logger = LoggerFactory.getLogger(TradeStatusService.class);

    @Autowired
    private TradeStatusRepository tradeStatusRepository;

    public List<TradeStatus> findAll() {
        logger.info("Retrieving all trade statuses");
        return tradeStatusRepository.findAll();
    }

    public Optional<TradeStatus> findById(Long id) {
        logger.debug("Retrieving trade status by id: {}", id);
        return tradeStatusRepository.findById(id);
    }

    public TradeStatus save(TradeStatus tradeStatus) {
        logger.info("Saving trade status: {}", tradeStatus);
        return tradeStatusRepository.save(tradeStatus);
    }

    public void deleteById(Long id) {
        logger.warn("Deleting trade status with id: {}", id);
        tradeStatusRepository.deleteById(id);
    }
}
