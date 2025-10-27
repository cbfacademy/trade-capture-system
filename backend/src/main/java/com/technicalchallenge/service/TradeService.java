package com.technicalchallenge.service;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.model.*;
import com.technicalchallenge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TradeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private TradeLegRepository tradeLegRepository;
    @Autowired
    private CashflowRepository cashflowRepository;
    @Autowired
    private TradeStatusRepository tradeStatusRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CounterpartyRepository counterpartyRepository;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private TradeTypeRepository tradeTypeRepository;
    @Autowired
    private TradeSubTypeRepository tradeSubTypeRepository;
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
    @Autowired
    private AdditionalInfoService additionalInfoService;

    public List<Trade> getAllTrades() {
        logger.info("Retrieving all trades");
        return tradeRepository.findAll();
    }

    public Optional<Trade> getTradeById(Long tradeId) {
        logger.debug("Retrieving trade by id: {}", tradeId);
        return tradeRepository.findByTradeIdAndActiveTrue(tradeId);
    }

    @Transactional
    public Trade createTrade(TradeDTO tradeDTO) {
        logger.info("Creating new trade with ID: {}", tradeDTO.getTradeId());

        // Generate trade ID if not provided
        if (tradeDTO.getTradeId() == null) {
            // Generate sequential trade ID starting from 10000
            Long generatedTradeId = generateNextTradeId();
            tradeDTO.setTradeId(generatedTradeId);
            logger.info("Generated trade ID: {}", generatedTradeId);
        }

        // Validate business rules
        validateTradeCreation(tradeDTO);

        // Create trade entity
        Trade trade = mapDTOToEntity(tradeDTO);
        trade.setVersion(1);
        trade.setActive(true);
        trade.setCreatedDate(LocalDateTime.now());
        trade.setLastTouchTimestamp(LocalDateTime.now());

        // Set default trade status to NEW if not provided
        if (tradeDTO.getTradeStatus() == null) {
            tradeDTO.setTradeStatus("NEW");
        }

        // Populate reference data
        populateReferenceDataByName(trade, tradeDTO);

        // Ensure we have essential reference data
        validateReferenceData(trade);

        Trade savedTrade = tradeRepository.save(trade);

        // Create trade legs and cashflows
        createTradeLegsWithCashflows(tradeDTO, savedTrade);


        logger.info("Successfully created trade with ID: {}", savedTrade.getTradeId());
        return savedTrade;
    }

    // NEW METHOD: For controller compatibility
    @Transactional
    public Trade saveTrade(Trade trade, TradeDTO tradeDTO) {
        logger.info("Saving trade with ID: {}", trade.getTradeId());

        // If this is an existing trade (has ID), handle as amendment
        if (trade.getId() != null) {
            return amendTrade(trade.getTradeId(), tradeDTO);
        } else {
            return createTrade(tradeDTO);
        }
    }

    // FIXED: Populate reference data by names from DTO
    public void populateReferenceDataByName(Trade trade, TradeDTO tradeDTO) {
        logger.debug("Populating reference data for trade");

        // Populate Book
        if (tradeDTO.getBookName() != null) {
            bookRepository.findByBookName(tradeDTO.getBookName())
                    .ifPresent(trade::setBook);
        } else if (tradeDTO.getBookId() != null) {
            bookRepository.findById(tradeDTO.getBookId())
                    .ifPresent(trade::setBook);
        }

        // Populate Counterparty
        if (tradeDTO.getCounterpartyName() != null) {
            counterpartyRepository.findByName(tradeDTO.getCounterpartyName())
                    .ifPresent(trade::setCounterparty);
        } else if (tradeDTO.getCounterpartyId() != null) {
            counterpartyRepository.findById(tradeDTO.getCounterpartyId())
                    .ifPresent(trade::setCounterparty);
        }

        // Populate TradeStatus
        if (tradeDTO.getTradeStatus() != null) {
            tradeStatusRepository.findByTradeStatus(tradeDTO.getTradeStatus())
                    .ifPresent(trade::setTradeStatus);
        } else if (tradeDTO.getTradeStatusId() != null) {
            tradeStatusRepository.findById(tradeDTO.getTradeStatusId())
                    .ifPresent(trade::setTradeStatus);
        }

        // Populate other reference data
        populateUserReferences(trade, tradeDTO);
        populateTradeTypeReferences(trade, tradeDTO);
    }

    private void populateUserReferences(Trade trade, TradeDTO tradeDTO) {
        // Handle trader user by name or ID with enhanced logging
        if (tradeDTO.getTraderUserName() != null) {
            logger.debug("Looking up trader user by name: {}", tradeDTO.getTraderUserName());
            String[] nameParts = tradeDTO.getTraderUserName().trim().split("\\s+");
            if (nameParts.length >= 1) {
                String firstName = nameParts[0];
                logger.debug("Searching for user with firstName: {}", firstName);
                Optional<ApplicationUser> userOpt = applicationUserRepository.findByFirstName(firstName);
                if (userOpt.isPresent()) {
                    trade.setTraderUser(userOpt.get());
                    logger.debug("Found trader user: {} {}", userOpt.get().getFirstName(), userOpt.get().getLastName());
                } else {
                    logger.warn("Trader user not found with firstName: {}", firstName);
                    // Try with loginId as fallback
                    Optional<ApplicationUser> byLoginId = applicationUserRepository.findByLoginId(tradeDTO.getTraderUserName().toLowerCase());
                    if (byLoginId.isPresent()) {
                        trade.setTraderUser(byLoginId.get());
                        logger.debug("Found trader user by loginId: {}", tradeDTO.getTraderUserName());
                    } else {
                        logger.warn("Trader user not found by loginId either: {}", tradeDTO.getTraderUserName());
                    }
                }
            }
        } else if (tradeDTO.getTraderUserId() != null) {
            applicationUserRepository.findById(tradeDTO.getTraderUserId())
                    .ifPresent(trade::setTraderUser);
        }

        // Handle inputter user by name or ID with enhanced logging
        if (tradeDTO.getInputterUserName() != null) {
            logger.debug("Looking up inputter user by name: {}", tradeDTO.getInputterUserName());
            String[] nameParts = tradeDTO.getInputterUserName().trim().split("\\s+");
            if (nameParts.length >= 1) {
                String firstName = nameParts[0];
                logger.debug("Searching for inputter with firstName: {}", firstName);
                Optional<ApplicationUser> userOpt = applicationUserRepository.findByFirstName(firstName);
                if (userOpt.isPresent()) {
                    trade.setTradeInputterUser(userOpt.get());
                    logger.debug("Found inputter user: {} {}", userOpt.get().getFirstName(), userOpt.get().getLastName());
                } else {
                    logger.warn("Inputter user not found with firstName: {}", firstName);
                    // Try with loginId as fallback
                    Optional<ApplicationUser> byLoginId = applicationUserRepository.findByLoginId(tradeDTO.getInputterUserName().toLowerCase());
                    if (byLoginId.isPresent()) {
                        trade.setTradeInputterUser(byLoginId.get());
                        logger.debug("Found inputter user by loginId: {}", tradeDTO.getInputterUserName());
                    } else {
                        logger.warn("Inputter user not found by loginId either: {}", tradeDTO.getInputterUserName());
                    }
                }
            }
        } else if (tradeDTO.getTradeInputterUserId() != null) {
            applicationUserRepository.findById(tradeDTO.getTradeInputterUserId())
                    .ifPresent(trade::setTradeInputterUser);
        }
    }

    private void populateTradeTypeReferences(Trade trade, TradeDTO tradeDTO) {
        if (tradeDTO.getTradeType() != null) {
            logger.debug("Looking up trade type: {}", tradeDTO.getTradeType());
            Optional<TradeType> tradeTypeOpt = tradeTypeRepository.findByTradeType(tradeDTO.getTradeType());
            if (tradeTypeOpt.isPresent()) {
                trade.setTradeType(tradeTypeOpt.get());
                logger.debug("Found trade type: {} with ID: {}", tradeTypeOpt.get().getTradeType(), tradeTypeOpt.get().getId());
            } else {
                logger.warn("Trade type not found: {}", tradeDTO.getTradeType());
            }
        } else if (tradeDTO.getTradeTypeId() != null) {
            tradeTypeRepository.findById(tradeDTO.getTradeTypeId())
                    .ifPresent(trade::setTradeType);
        }

        if (tradeDTO.getTradeSubType() != null) {
            Optional<TradeSubType> tradeSubTypeOpt = tradeSubTypeRepository.findByTradeSubType(tradeDTO.getTradeSubType());
            if (tradeSubTypeOpt.isPresent()) {
                trade.setTradeSubType(tradeSubTypeOpt.get());
            } else {
                List<TradeSubType> allSubTypes = tradeSubTypeRepository.findAll();
                for (TradeSubType subType : allSubTypes) {
                    if (subType.getTradeSubType().equalsIgnoreCase(tradeDTO.getTradeSubType())) {
                        trade.setTradeSubType(subType);
                        break;
                    }
                }
            }
        } else if (tradeDTO.getTradeSubTypeId() != null) {
            tradeSubTypeRepository.findById(tradeDTO.getTradeSubTypeId())
                    .ifPresent(trade::setTradeSubType);
        }
    }

    // NEW METHOD: Delete trade (mark as cancelled)
    @Transactional
    public void deleteTrade(Long tradeId) {
        logger.info("Deleting (cancelling) trade with ID: {}", tradeId);
        cancelTrade(tradeId);
    }

    @Transactional
    public Trade amendTrade(Long tradeId, TradeDTO tradeDTO) {
        logger.info("Amending trade with ID: {}", tradeId);

        Optional<Trade> existingTradeOpt = getTradeById(tradeId);
        if (existingTradeOpt.isEmpty()) {
            throw new RuntimeException("Trade not found: " + tradeId);
        }

        Trade existingTrade = existingTradeOpt.get();

        // Deactivate existing trade
        existingTrade.setActive(false);
        existingTrade.setDeactivatedDate(LocalDateTime.now());
        tradeRepository.save(existingTrade);

        // Create new version
        Trade amendedTrade = mapDTOToEntity(tradeDTO);
        amendedTrade.setTradeId(tradeId);
        amendedTrade.setVersion(existingTrade.getVersion() + 1);
        amendedTrade.setActive(true);
        amendedTrade.setCreatedDate(LocalDateTime.now());
        amendedTrade.setLastTouchTimestamp(LocalDateTime.now());

        // Populate reference data
        populateReferenceDataByName(amendedTrade, tradeDTO);

        // Set status to AMENDED
        TradeStatus amendedStatus = tradeStatusRepository.findByTradeStatus("AMENDED")
                .orElseThrow(() -> new RuntimeException("AMENDED status not found"));
        amendedTrade.setTradeStatus(amendedStatus);

        Trade savedTrade = tradeRepository.save(amendedTrade);

        // Create new trade legs and cashflows
        createTradeLegsWithCashflows(tradeDTO, savedTrade);


        logger.info("Successfully amended trade with ID: {}", savedTrade.getTradeId());
        return savedTrade;
    }

    @Transactional
    public Trade terminateTrade(Long tradeId) {
        logger.info("Terminating trade with ID: {}", tradeId);

        Optional<Trade> tradeOpt = getTradeById(tradeId);
        if (tradeOpt.isEmpty()) {
            throw new RuntimeException("Trade not found: " + tradeId);
        }

        Trade trade = tradeOpt.get();
        TradeStatus terminatedStatus = tradeStatusRepository.findByTradeStatus("TERMINATED")
                .orElseThrow(() -> new RuntimeException("TERMINATED status not found"));

        trade.setTradeStatus(terminatedStatus);
        trade.setLastTouchTimestamp(LocalDateTime.now());

        return tradeRepository.save(trade);
    }

    @Transactional
    public Trade cancelTrade(Long tradeId) {
        logger.info("Cancelling trade with ID: {}", tradeId);

        Optional<Trade> tradeOpt = getTradeById(tradeId);
        if (tradeOpt.isEmpty()) {
            throw new RuntimeException("Trade not found: " + tradeId);
        }

        Trade trade = tradeOpt.get();
        TradeStatus cancelledStatus = tradeStatusRepository.findByTradeStatus("CANCELLED")
                .orElseThrow(() -> new RuntimeException("CANCELLED status not found"));

        trade.setTradeStatus(cancelledStatus);
        trade.setLastTouchTimestamp(LocalDateTime.now());

        return tradeRepository.save(trade);
    }

    private void validateTradeCreation(TradeDTO tradeDTO) {
        // Validate dates - Fixed to use consistent field names
        if (tradeDTO.getTradeStartDate() != null && tradeDTO.getTradeDate() != null) {
            if (tradeDTO.getTradeStartDate().isBefore(tradeDTO.getTradeDate())) {
                throw new RuntimeException("Start date cannot be before trade date");
            }
        }
        if (tradeDTO.getTradeMaturityDate() != null && tradeDTO.getTradeStartDate() != null) {
            if (tradeDTO.getTradeMaturityDate().isBefore(tradeDTO.getTradeStartDate())) {
                throw new RuntimeException("Maturity date cannot be before start date");
            }
        }

        // Validate trade has exactly 2 legs
        if (tradeDTO.getTradeLegs() == null || tradeDTO.getTradeLegs().size() != 2) {
            throw new RuntimeException("Trade must have exactly 2 legs");
        }
    }

    private Trade mapDTOToEntity(TradeDTO dto) {
        Trade trade = new Trade();
        trade.setTradeId(dto.getTradeId());
        trade.setTradeDate(dto.getTradeDate()); // Fixed field names
        trade.setTradeStartDate(dto.getTradeStartDate());
        trade.setTradeMaturityDate(dto.getTradeMaturityDate());
        trade.setTradeExecutionDate(dto.getTradeExecutionDate());
        trade.setUtiCode(dto.getUtiCode());
        trade.setValidityStartDate(dto.getValidityStartDate());
        trade.setLastTouchTimestamp(LocalDateTime.now());
        return trade;
    }

    private void createTradeLegsWithCashflows(TradeDTO tradeDTO, Trade savedTrade) {
        for (int i = 0; i < tradeDTO.getTradeLegs().size(); i++) {
            var legDTO = tradeDTO.getTradeLegs().get(i);

            TradeLeg tradeLeg = new TradeLeg();
            tradeLeg.setTrade(savedTrade);
            tradeLeg.setNotional(legDTO.getNotional());
            tradeLeg.setRate(legDTO.getRate());
            tradeLeg.setActive(true);
            tradeLeg.setCreatedDate(LocalDateTime.now());

            // Populate reference data for leg
            populateLegReferenceData(tradeLeg, legDTO);

            TradeLeg savedLeg = tradeLegRepository.save(tradeLeg);

            // Generate cashflows for this leg
            if (tradeDTO.getTradeStartDate() != null && tradeDTO.getTradeMaturityDate() != null) {
                generateCashflows(savedLeg, tradeDTO.getTradeStartDate(), tradeDTO.getTradeMaturityDate());
            }
        }
    }

    private void populateLegReferenceData(TradeLeg leg, TradeLegDTO legDTO) {
        // Populate currency by name or ID
        if (legDTO.getCurrency() != null) {
            currencyRepository.findByCurrency(legDTO.getCurrency())
                    .ifPresent(leg::setCurrency);
        } else if (legDTO.getCurrencyId() != null) {
            currencyRepository.findById(legDTO.getCurrencyId())
                    .ifPresent(leg::setCurrency);
        }

        // Populate leg type by name or ID
        if (legDTO.getLegType() != null) {
            legTypeRepository.findByType(legDTO.getLegType())
                    .ifPresent(leg::setLegRateType);
        } else if (legDTO.getLegTypeId() != null) {
            legTypeRepository.findById(legDTO.getLegTypeId())
                    .ifPresent(leg::setLegRateType);
        }

        // Populate index by name or ID
        if (legDTO.getIndexName() != null) {
            indexRepository.findByIndex(legDTO.getIndexName())
                    .ifPresent(leg::setIndex);
        } else if (legDTO.getIndexId() != null) {
            indexRepository.findById(legDTO.getIndexId())
                    .ifPresent(leg::setIndex);
        }

        // Populate holiday calendar by name or ID
        if (legDTO.getHolidayCalendar() != null) {
            holidayCalendarRepository.findByHolidayCalendar(legDTO.getHolidayCalendar())
                    .ifPresent(leg::setHolidayCalendar);
        } else if (legDTO.getHolidayCalendarId() != null) {
            holidayCalendarRepository.findById(legDTO.getHolidayCalendarId())
                    .ifPresent(leg::setHolidayCalendar);
        }

        // Populate schedule by name or ID
        if (legDTO.getCalculationPeriodSchedule() != null) {
            scheduleRepository.findBySchedule(legDTO.getCalculationPeriodSchedule())
                    .ifPresent(leg::setCalculationPeriodSchedule);
        } else if (legDTO.getScheduleId() != null) {
            scheduleRepository.findById(legDTO.getScheduleId())
                    .ifPresent(leg::setCalculationPeriodSchedule);
        }

        // Populate payment business day convention by name or ID
        if (legDTO.getPaymentBusinessDayConvention() != null) {
            businessDayConventionRepository.findByBdc(legDTO.getPaymentBusinessDayConvention())
                    .ifPresent(leg::setPaymentBusinessDayConvention);
        } else if (legDTO.getPaymentBdcId() != null) {
            businessDayConventionRepository.findById(legDTO.getPaymentBdcId())
                    .ifPresent(leg::setPaymentBusinessDayConvention);
        }

        // Populate fixing business day convention by name or ID
        if (legDTO.getFixingBusinessDayConvention() != null) {
            businessDayConventionRepository.findByBdc(legDTO.getFixingBusinessDayConvention())
                    .ifPresent(leg::setFixingBusinessDayConvention);
        } else if (legDTO.getFixingBdcId() != null) {
            businessDayConventionRepository.findById(legDTO.getFixingBdcId())
                    .ifPresent(leg::setFixingBusinessDayConvention);
        }

        // Populate pay/receive flag by name or ID
        if (legDTO.getPayReceiveFlag() != null) {
            payRecRepository.findByPayRec(legDTO.getPayReceiveFlag())
                    .ifPresent(leg::setPayReceiveFlag);
        } else if (legDTO.getPayRecId() != null) {
            payRecRepository.findById(legDTO.getPayRecId())
                    .ifPresent(leg::setPayReceiveFlag);
        }
    }

    /**
     * FIXED: Generate cashflows based on schedule and maturity date
     */
    void generateCashflows(TradeLeg leg, LocalDate startDate, LocalDate maturityDate) {
        logger.info("Generating cashflows for leg {} from {} to {}", leg.getLegId(), startDate, maturityDate);

        // Use default schedule if not set
        String schedule = "3M"; // Default to quarterly
        if (leg.getCalculationPeriodSchedule() != null) {
            schedule = leg.getCalculationPeriodSchedule().getSchedule();
        }

        int monthsInterval = parseSchedule(schedule);
        List<LocalDate> paymentDates = calculatePaymentDates(startDate, maturityDate, monthsInterval);

        for (LocalDate paymentDate : paymentDates) {
            Cashflow cashflow = new Cashflow();
            cashflow.setTradeLeg(leg); // Fixed field name
            cashflow.setValueDate(paymentDate);
            cashflow.setRate(leg.getRate());

            // Calculate value based on leg type
            BigDecimal cashflowValue = calculateCashflowValue(leg, monthsInterval);
            cashflow.setPaymentValue(cashflowValue);

            cashflow.setPayRec(leg.getPayReceiveFlag());
            cashflow.setPaymentBusinessDayConvention(leg.getPaymentBusinessDayConvention());
            cashflow.setCreatedDate(LocalDateTime.now());
            cashflow.setActive(true);

            cashflowRepository.save(cashflow);
        }

        logger.info("Generated {} cashflows for leg {}", paymentDates.size(), leg.getLegId());
    }

    private int parseSchedule(String schedule) {
        if (schedule == null || schedule.trim().isEmpty()) {
            return 3; // Default to quarterly
        }

        schedule = schedule.trim();

        // Handle common schedule names
        switch (schedule.toLowerCase()) {
            case "monthly":
                return 1;
            case "quarterly":
                return 3;
            case "semi-annually":
            case "semiannually":
            case "half-yearly":
                return 6;
            case "annually":
            case "yearly":
                return 12;
            default:
                // Parse "1M", "3M", "12M" format
                if (schedule.endsWith("M") || schedule.endsWith("m")) {
                    try {
                        return Integer.parseInt(schedule.substring(0, schedule.length() - 1));
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid schedule format: " + schedule);
                    }
                }
                throw new RuntimeException("Invalid schedule format: " + schedule + ". Supported formats: Monthly, Quarterly, Semi-annually, Annually, or 1M, 3M, 6M, 12M");
        }
    }

    private List<LocalDate> calculatePaymentDates(LocalDate startDate, LocalDate maturityDate, int monthsInterval) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate.plusMonths(monthsInterval);

        while (!currentDate.isAfter(maturityDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusMonths(monthsInterval);
        }

        return dates;
    }

    private BigDecimal calculateCashflowValue(TradeLeg leg, int monthsInterval) {
        if (leg.getLegRateType() == null) {
            return BigDecimal.ZERO;
        }

        String legType = leg.getLegRateType().getType();

        if ("Fixed".equals(legType)) {
            // FIXED: Use BigDecimal for precise monetary calculations
            BigDecimal notional = leg.getNotional();
            
            // FIXED: Convert percentage rate to decimal (3.5% -> 0.035)
            BigDecimal rate = BigDecimal.valueOf(leg.getRate()).divide(BigDecimal.valueOf(100));
            
            BigDecimal months = BigDecimal.valueOf(monthsInterval);
            BigDecimal twelve = BigDecimal.valueOf(12);

            // FIXED: Precise calculation using BigDecimal arithmetic
            // Formula: (notional * rate * months) / 12
            BigDecimal result = notional
                .multiply(rate)
                .multiply(months)
                .divide(twelve, 2, java.math.RoundingMode.HALF_UP);

            return result;
        } else if ("Floating".equals(legType)) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }

    private void validateReferenceData(Trade trade) {
        // Validate essential reference data is populated
        if (trade.getBook() == null) {
            throw new RuntimeException("Book not found or not set");
        }
        if (trade.getCounterparty() == null) {
            throw new RuntimeException("Counterparty not found or not set");
        }
        if (trade.getTradeStatus() == null) {
            throw new RuntimeException("Trade status not found or not set");
        }

        logger.debug("Reference data validation passed for trade");
    }

    // NEW METHOD: Generate the next trade ID (sequential)
    private Long generateNextTradeId() {
        // For simplicity, using a static variable. In real scenario, this should be atomic and thread-safe.
        return 10000L + tradeRepository.count();
    }

    // Enhancement 1: Advanced Trade Search System (simplified implementation)

    /**
     * Search trades by counterparty name
     */
    public List<Trade> searchTradesByCounterparty(String counterpartyName) {
        logger.info("Searching trades by counterparty: {}", counterpartyName);
        return tradeRepository.findByCounterpartyNameContaining(counterpartyName);
    }

    /**
     * Search trades by book name
     */
    public List<Trade> searchTradesByBook(String bookName) {
        logger.info("Searching trades by book: {}", bookName);
        return tradeRepository.findByBookNameContaining(bookName);
    }

    /**
     * Search trades by status
     */
    public List<Trade> searchTradesByStatus(String status) {
        logger.info("Searching trades by status: {}", status);
        return tradeRepository.findByTradeStatus(status);
    }

    /**
     * Search trades by date range
     */
    public List<Trade> searchTradesByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Searching trades from {} to {}", startDate, endDate);
        return tradeRepository.findByTradeDateBetween(startDate, endDate);
    }

    /**
     * Get trades for a specific trader (for dashboard)
     */
    public List<Trade> getTradesByTrader(Long userId) {
        logger.info("Retrieving trades for trader user ID: {}", userId);
        return tradeRepository.findByTraderUserId(userId);
    }

    /**
     * Get paginated trades
     */
    public org.springframework.data.domain.Page<Trade> getTradesPaginated(org.springframework.data.domain.Pageable pageable) {
        logger.info("Retrieving paginated trades");
        return tradeRepository.findAllActivePaginated(pageable);
    }

    /**
     * Multi-criteria search (combining individual searches)
     */
    public List<Trade> searchTrades(String counterpartyName, String bookName, String status,
                                   LocalDate startDate, LocalDate endDate) {
        logger.info("Multi-criteria search - counterparty: {}, book: {}, status: {}, dates: {} to {}",
                   counterpartyName, bookName, status, startDate, endDate);
        
        // Start with all active trades
        List<Trade> results = getAllTrades();
        
        // Apply filters sequentially
        if (counterpartyName != null && !counterpartyName.trim().isEmpty()) {
            results = results.stream()
                .filter(t -> t.getCounterparty() != null &&
                           t.getCounterparty().getName().toLowerCase().contains(counterpartyName.toLowerCase()))
                .toList();
        }
        
        if (bookName != null && !bookName.trim().isEmpty()) {
            results = results.stream()
                .filter(t -> t.getBook() != null &&
                           t.getBook().getBookName().toLowerCase().contains(bookName.toLowerCase()))
                .toList();
        }
        
        if (status != null && !status.trim().isEmpty()) {
            results = results.stream()
                .filter(t -> t.getTradeStatus() != null &&
                           t.getTradeStatus().getTradeStatus().toLowerCase().contains(status.toLowerCase()))
                .toList();
        }
        
        if (startDate != null) {
            results = results.stream()
                .filter(t -> t.getTradeDate() != null && !t.getTradeDate().isBefore(startDate))
                .toList();
        }
        
        if (endDate != null) {
            results = results.stream()
                .filter(t -> t.getTradeDate() != null && !t.getTradeDate().isAfter(endDate))
                .toList();
        }
        
        return results;
    }

    /**
     * Simple RSQL-style query support
     */
    public List<Trade> searchTradesRsql(String query) {
        logger.info("RSQL query search: {}", query);
        
        // Simple implementation for common RSQL patterns
        try {
            if (query.contains("counterparty.name==")) {
                String counterpartyName = extractSimpleValue(query, "counterparty.name==");
                return searchTradesByCounterparty(counterpartyName);
            }
            
            if (query.contains("tradeStatus.tradeStatus==")) {
                String status = extractSimpleValue(query, "tradeStatus.tradeStatus==");
                return searchTradesByStatus(status);
            }
            
            if (query.contains("tradeDate=ge=") && query.contains("tradeDate=le=")) {
                String startDateStr = extractSimpleValue(query, "tradeDate=ge=");
                String endDateStr = extractSimpleValue(query, "tradeDate=le=");
                LocalDate startDate = LocalDate.parse(startDateStr);
                LocalDate endDate = LocalDate.parse(endDateStr);
                return searchTradesByDateRange(startDate, endDate);
            }
            
            // If no specific pattern matches, return all trades
            return getAllTrades();
            
        } catch (Exception e) {
            logger.error("Error parsing RSQL query: {}", query, e);
            throw new RuntimeException("Invalid RSQL query format: " + e.getMessage());
        }
    }
    
    private String extractSimpleValue(String query, String key) {
        int startIndex = query.indexOf(key) + key.length();
        int endIndex = startIndex;
        
        // Find end of value (semicolon, comma, or end of string)
        while (endIndex < query.length() &&
               query.charAt(endIndex) != ';' &&
               query.charAt(endIndex) != ',' &&
               query.charAt(endIndex) != ')') {
            endIndex++;
        }
        
        return query.substring(startIndex, endIndex).trim()
    }

}
