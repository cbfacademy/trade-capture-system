package com.technicalchallenge.service;

import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.repository.TradeLegRepository;
import com.technicalchallenge.repository.CurrencyRepository;
import com.technicalchallenge.repository.LegTypeRepository;
import com.technicalchallenge.repository.IndexRepository;
import com.technicalchallenge.repository.HolidayCalendarRepository;
import com.technicalchallenge.repository.ScheduleRepository;
import com.technicalchallenge.repository.BusinessDayConventionRepository;
import com.technicalchallenge.repository.PayRecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class TradeLegService {
    private static final Logger logger = LoggerFactory.getLogger(TradeLegService.class);

    @Autowired
    private TradeLegRepository tradeLegRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private LegTypeRepository legTypeRepository;
    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private HolidayCalendarRepository holidayCalendarRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private BusinessDayConventionRepository businessDayConventionRepository;
    @Autowired
    private PayRecRepository payRecRepository;

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
