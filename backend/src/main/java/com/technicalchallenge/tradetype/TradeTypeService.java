package com.technicalchallenge.tradetype;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeTypeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeTypeService.class);

    @Autowired
    private TradeTypeRepository tradeTypeRepository;

    public List<TradeType> findAll() {
        logger.info("Retrieving all trade types");
        return tradeTypeRepository.findAll();
    }

    public Optional<TradeType> findById(Long id) {
        logger.debug("Retrieving trade type by id: {}", id);
        return tradeTypeRepository.findById(id);
    }

    public TradeType save(TradeType tradeType) {
        logger.info("Saving trade type: {}", tradeType);
        return tradeTypeRepository.save(tradeType);
    }

    public void deleteById(Long id) {
        logger.warn("Deleting trade type with id: {}", id);
        tradeTypeRepository.deleteById(id);
    }
}
