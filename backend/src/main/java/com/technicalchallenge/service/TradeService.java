package com.technicalchallenge.service;

import com.technicalchallenge.dto.DailySummaryDTO;
import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.dto.TradeSummaryDTO;
import com.technicalchallenge.dto.ValidationResult;
import com.technicalchallenge.model.*;
import com.technicalchallenge.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TradeService - manages trade lifecycle, search, validation hook and portfolio summaries.
 *
 * NOTES:
 *  - This file includes CHANGED/NEW sections to implement Step 3 enhancements (search, validation, dashboard)
 *  - Markers such as "=== NEW ===" and "=== CHANGED ===" annotate areas that were added/modified.
 */
@Service
@Transactional
public class TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

    // === REPOSITORIES (unchanged core + added where needed) ===
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

    // === NEW: TradeValidationService (CHANGED/NEW) ===
    @Autowired
    private TradeValidationService tradeValidationService; // === NEW ===

    // --------------------------- CRUD ---------------------------
    public List<Trade> getAllTrades() {
        logger.info("Retrieving all trades");
        return tradeRepository.findAll();
    }

    public Optional<Trade> getTradeById(Long tradeId) {
        logger.debug("Retrieving trade by id: {}", tradeId);
        return tradeRepository.findByTradeIdAndActiveTrue(tradeId);
    }

    // === CHANGED: createTrade now uses TradeValidationService before persisting ===
    @Transactional
    public Trade createTrade(TradeDTO tradeDTO) {
        logger.info("Creating new trade with DTO: {}", tradeDTO);

        // === NEW: Business validation hook ===
        ValidationResult vr = tradeValidationService.validateTradeBusinessRules(tradeDTO);
        if (!vr.isValid()) {
            String msg = "Validation failed: " + String.join("; ", vr.getErrors());
            logger.warn(msg);
            throw new RuntimeException(msg);
        }

        // Generate trade ID if not provided
        if (tradeDTO.getTradeId() == null) {
            Long generatedTradeId = generateNextTradeId();
            tradeDTO.setTradeId(generatedTradeId);
            logger.info("Generated trade ID: {}", generatedTradeId);
        }

        // Domain-level validations (keeps previous behavior)
        validateTradeCreation(tradeDTO);

        Trade trade = mapDTOToEntity(tradeDTO);
        trade.setVersion(1);
        trade.setActive(true);
        trade.setCreatedDate(LocalDateTime.now());
        trade.setLastTouchTimestamp(LocalDateTime.now());

        if (tradeDTO.getTradeStatus() == null) {
            tradeDTO.setTradeStatus("NEW");
        }

        populateReferenceDataByName(trade, tradeDTO);
        validateReferenceData(trade);

        Trade savedTrade = tradeRepository.save(trade);
        createTradeLegsWithCashflows(tradeDTO, savedTrade);

        logger.info("Successfully created trade with ID: {}", savedTrade.getTradeId());
        return savedTrade;
    }

    // === NEW: saveTrade used by controllers to accept either DTO->entity flow or amend ===
    @Transactional
    public Trade saveTrade(Trade trade, TradeDTO tradeDTO) {
        logger.info("Saving trade (controller compatibility). trade.tradeId={}", trade.getTradeId());
        if (trade.getId() != null) return amendTrade(trade.getTradeId(), tradeDTO);
        return createTrade(tradeDTO);
    }

    // === CHANGED/NEW: populateReferenceDataByName retains existing functionality ===
    public void populateReferenceDataByName(Trade trade, TradeDTO tradeDTO) {
        logger.debug("Populating reference data for trade");

        if (tradeDTO.getBookName() != null) {
            bookRepository.findByBookName(tradeDTO.getBookName()).ifPresent(trade::setBook);
        } else if (tradeDTO.getBookId() != null) {
            bookRepository.findById(tradeDTO.getBookId()).ifPresent(trade::setBook);
        }

        if (tradeDTO.getCounterpartyName() != null) {
            counterpartyRepository.findByName(tradeDTO.getCounterpartyName()).ifPresent(trade::setCounterparty);
        } else if (tradeDTO.getCounterpartyId() != null) {
            counterpartyRepository.findById(tradeDTO.getCounterpartyId()).ifPresent(trade::setCounterparty);
        }

        if (tradeDTO.getTradeStatus() != null) {
            tradeStatusRepository.findByTradeStatus(tradeDTO.getTradeStatus()).ifPresent(trade::setTradeStatus);
        } else if (tradeDTO.getTradeStatusId() != null) {
            tradeStatusRepository.findById(tradeDTO.getTradeStatusId()).ifPresent(trade::setTradeStatus);
        }

        populateUserReferences(trade, tradeDTO);
        populateTradeTypeReferences(trade, tradeDTO);
    }

    private void populateUserReferences(Trade trade, TradeDTO tradeDTO) {
        if (tradeDTO.getTraderUserName() != null) {
            String[] nameParts = tradeDTO.getTraderUserName().trim().split("\\s+");
            if (nameParts.length >= 1) {
                String firstName = nameParts[0];
                applicationUserRepository.findByFirstName(firstName).ifPresent(trade::setTraderUser);
            }
        } else if (tradeDTO.getTraderUserId() != null) {
            applicationUserRepository.findById(tradeDTO.getTraderUserId()).ifPresent(trade::setTraderUser);
        }

        if (tradeDTO.getInputterUserName() != null) {
            String[] nameParts = tradeDTO.getInputterUserName().trim().split("\\s+");
            if (nameParts.length >= 1) {
                String firstName = nameParts[0];
                applicationUserRepository.findByFirstName(firstName).ifPresent(trade::setTradeInputterUser);
            }
        } else if (tradeDTO.getTradeInputterUserId() != null) {
            applicationUserRepository.findById(tradeDTO.getTradeInputterUserId()).ifPresent(trade::setTradeInputterUser);
        }
    }

    private void populateTradeTypeReferences(Trade trade, TradeDTO tradeDTO) {
        if (tradeDTO.getTradeType() != null) {
            tradeTypeRepository.findByTradeType(tradeDTO.getTradeType()).ifPresent(trade::setTradeType);
        } else if (tradeDTO.getTradeTypeId() != null) {
            tradeTypeRepository.findById(tradeDTO.getTradeTypeId()).ifPresent(trade::setTradeType);
        }

        if (tradeDTO.getTradeSubType() != null) {
            tradeSubTypeRepository.findByTradeSubType(tradeDTO.getTradeSubType()).ifPresent(trade::setTradeSubType);
        } else if (tradeDTO.getTradeSubTypeId() != null) {
            tradeSubTypeRepository.findById(tradeDTO.getTradeSubTypeId()).ifPresent(trade::setTradeSubType);
        }
    }

    // === DELETION / LIFECYCLE ===
    @Transactional
    public void deleteTrade(Long tradeId) {
        logger.info("Deleting (cancelling) trade with ID: {}", tradeId);
        cancelTrade(tradeId);
    }

    @Transactional
    public Trade amendTrade(Long tradeId, TradeDTO tradeDTO) {
        logger.info("Amending trade with ID: {}", tradeId);

        Optional<Trade> existingTradeOpt = getTradeById(tradeId);
        if (existingTradeOpt.isEmpty()) throw new RuntimeException("Trade not found: " + tradeId);

        Trade existingTrade = existingTradeOpt.get();

        // Deactivate existing
        existingTrade.setActive(false);
        existingTrade.setDeactivatedDate(LocalDateTime.now());
        tradeRepository.save(existingTrade);

        Trade amendedTrade = mapDTOToEntity(tradeDTO);
        amendedTrade.setTradeId(tradeId);
        amendedTrade.setVersion((existingTrade.getVersion() == null ? 0 : existingTrade.getVersion()) + 1);
        amendedTrade.setActive(true);
        amendedTrade.setCreatedDate(LocalDateTime.now());
        amendedTrade.setLastTouchTimestamp(LocalDateTime.now());

        populateReferenceDataByName(amendedTrade, tradeDTO);

        TradeStatus amendedStatus = tradeStatusRepository.findByTradeStatus("AMENDED")
                .orElseThrow(() -> new RuntimeException("AMENDED status not found"));
        amendedTrade.setTradeStatus(amendedStatus);

        Trade savedTrade = tradeRepository.save(amendedTrade);
        createTradeLegsWithCashflows(tradeDTO, savedTrade);

        logger.info("Successfully amended trade with ID: {}", savedTrade.getTradeId());
        return savedTrade;
    }

    @Transactional
    public Trade terminateTrade(Long tradeId) {
        logger.info("Terminating trade with ID: {}", tradeId);
        Optional<Trade> tradeOpt = getTradeById(tradeId);
        if (tradeOpt.isEmpty()) throw new RuntimeException("Trade not found: " + tradeId);

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
        if (tradeOpt.isEmpty()) throw new RuntimeException("Trade not found: " + tradeId);

        Trade trade = tradeOpt.get();
        TradeStatus cancelledStatus = tradeStatusRepository.findByTradeStatus("CANCELLED")
                .orElseThrow(() -> new RuntimeException("CANCELLED status not found"));

        trade.setTradeStatus(cancelledStatus);
        trade.setLastTouchTimestamp(LocalDateTime.now());
        return tradeRepository.save(trade);
    }

    // === VALIDATIONS (existing) ===
    private void validateTradeCreation(TradeDTO tradeDTO) {
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

        if (tradeDTO.getTradeLegs() == null || tradeDTO.getTradeLegs().size() != 2) {
            throw new RuntimeException("Trade must have exactly 2 legs");
        }
    }

    private Trade mapDTOToEntity(TradeDTO dto) {
        Trade trade = new Trade();
        trade.setTradeId(dto.getTradeId());
        trade.setTradeDate(dto.getTradeDate());
        trade.setTradeStartDate(dto.getTradeStartDate());
        trade.setTradeMaturityDate(dto.getTradeMaturityDate());
        trade.setTradeExecutionDate(dto.getTradeExecutionDate());
        trade.setUtiCode(dto.getUtiCode());
        trade.setValidityStartDate(dto.getValidityStartDate());
        trade.setLastTouchTimestamp(LocalDateTime.now());
        return trade;
    }

    private void createTradeLegsWithCashflows(TradeDTO tradeDTO, Trade savedTrade) {
        if (tradeDTO.getTradeLegs() == null) return;
        for (TradeLegDTO legDTO : tradeDTO.getTradeLegs()) {
            TradeLeg tradeLeg = new TradeLeg();
            tradeLeg.setTrade(savedTrade);
            tradeLeg.setNotional(legDTO.getNotional());
            tradeLeg.setRate(legDTO.getRate());
            tradeLeg.setActive(true);
            tradeLeg.setCreatedDate(LocalDateTime.now());

            populateLegReferenceData(tradeLeg, legDTO);
            TradeLeg savedLeg = tradeLegRepository.save(tradeLeg);

            if (tradeDTO.getTradeStartDate() != null && tradeDTO.getTradeMaturityDate() != null) {
                generateCashflows(savedLeg, tradeDTO.getTradeStartDate(), tradeDTO.getTradeMaturityDate());
            }
        }
    }

    private void populateLegReferenceData(TradeLeg leg, TradeLegDTO legDTO) {
        if (legDTO.getCurrency() != null) currencyRepository.findByCurrency(legDTO.getCurrency()).ifPresent(leg::setCurrency);
        else if (legDTO.getCurrencyId() != null) currencyRepository.findById(legDTO.getCurrencyId()).ifPresent(leg::setCurrency);

        if (legDTO.getLegType() != null) legTypeRepository.findByType(legDTO.getLegType()).ifPresent(leg::setLegRateType);
        else if (legDTO.getLegTypeId() != null) legTypeRepository.findById(legDTO.getLegTypeId()).ifPresent(leg::setLegRateType);

        if (legDTO.getIndexName() != null) indexRepository.findByIndex(legDTO.getIndexName()).ifPresent(leg::setIndex);
        else if (legDTO.getIndexId() != null) indexRepository.findById(legDTO.getIndexId()).ifPresent(leg::setIndex);

        if (legDTO.getHolidayCalendar() != null) holidayCalendarRepository.findByHolidayCalendar(legDTO.getHolidayCalendar()).ifPresent(leg::setHolidayCalendar);
        else if (legDTO.getHolidayCalendarId() != null) holidayCalendarRepository.findById(legDTO.getHolidayCalendarId()).ifPresent(leg::setHolidayCalendar);

        if (legDTO.getCalculationPeriodSchedule() != null) scheduleRepository.findBySchedule(legDTO.getCalculationPeriodSchedule()).ifPresent(leg::setCalculationPeriodSchedule);
        else if (legDTO.getScheduleId() != null) scheduleRepository.findById(legDTO.getScheduleId()).ifPresent(leg::setCalculationPeriodSchedule);

        if (legDTO.getPaymentBusinessDayConvention() != null) businessDayConventionRepository.findByBdc(legDTO.getPaymentBusinessDayConvention()).ifPresent(leg::setPaymentBusinessDayConvention);
        else if (legDTO.getPaymentBdcId() != null) businessDayConventionRepository.findById(legDTO.getPaymentBdcId()).ifPresent(leg::setPaymentBusinessDayConvention);

        if (legDTO.getFixingBusinessDayConvention() != null) businessDayConventionRepository.findByBdc(legDTO.getFixingBusinessDayConvention()).ifPresent(leg::setFixingBusinessDayConvention);
        else if (legDTO.getFixingBdcId() != null) businessDayConventionRepository.findById(legDTO.getFixingBdcId()).ifPresent(leg::setFixingBusinessDayConvention);

        if (legDTO.getPayReceiveFlag() != null) payRecRepository.findByPayRec(legDTO.getPayReceiveFlag()).ifPresent(leg::setPayReceiveFlag);
        else if (legDTO.getPayRecId() != null) payRecRepository.findById(legDTO.getPayRecId()).ifPresent(leg::setPayReceiveFlag);
    }

    // === CASHFLOW GENERATION (unchanged logic but comments preserved) ===
    private void generateCashflows(TradeLeg leg, LocalDate startDate, LocalDate maturityDate) {
        logger.info("Generating cashflows for leg {} from {} to {}", leg.getLegId(), startDate, maturityDate);

        String schedule = "3M";
        if (leg.getCalculationPeriodSchedule() != null) schedule = leg.getCalculationPeriodSchedule().getSchedule();

        int monthsInterval = parseSchedule(schedule);
        List<LocalDate> paymentDates = calculatePaymentDates(startDate, maturityDate, monthsInterval);

        for (LocalDate paymentDate : paymentDates) {
            Cashflow cashflow = new Cashflow();
            cashflow.setTradeLeg(leg);
            cashflow.setValueDate(paymentDate);
            cashflow.setRate(leg.getRate());

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
        if (schedule == null || schedule.trim().isEmpty()) return 3;
        schedule = schedule.trim();
        switch (schedule.toLowerCase()) {
            case "monthly": return 1;
            case "quarterly": return 3;
            case "semi-annually": case "semiannually": case "half-yearly": return 6;
            case "annually": case "yearly": return 12;
            default:
                if (schedule.endsWith("M") || schedule.endsWith("m")) {
                    try { return Integer.parseInt(schedule.substring(0, schedule.length() - 1)); }
                    catch (NumberFormatException e) { throw new RuntimeException("Invalid schedule format: " + schedule); }
                }
                throw new RuntimeException("Invalid schedule format: " + schedule);
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
        if (leg.getLegRateType() == null) return BigDecimal.ZERO;
        String legType = leg.getLegRateType().getType();
        if ("Fixed".equals(legType)) {
            double notional = leg.getNotional() == null ? 0.0 : leg.getNotional().doubleValue();
            double rate = leg.getRate();
            double months = monthsInterval;
            double result = (notional * rate * months) / 12.0;
            return BigDecimal.valueOf(result);
        }
        return BigDecimal.ZERO;
    }

    private void validateReferenceData(Trade trade) {
        if (trade.getBook() == null) throw new RuntimeException("Book not found or not set");
        if (trade.getCounterparty() == null) throw new RuntimeException("Counterparty not found or not set");
        if (trade.getTradeStatus() == null) throw new RuntimeException("Trade status not found or not set");
        logger.debug("Reference data validation passed for trade");
    }

    private Long generateNextTradeId() {
        return 10000L + tradeRepository.count();
    }

    // === SEARCH / FILTER / RSQL (NEW) ===
    public List<Trade> searchTrades(String counterparty, String book, String trader, String status, LocalDate fromDate, LocalDate toDate) {
        Specification<Trade> spec = Specification.where(null);
        if (counterparty != null && !counterparty.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("counterparty").get("name"), counterparty));
        if (book != null && !book.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("book").get("bookName"), book));
        if (trader != null && !trader.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("traderUser").get("loginId"), trader));
        if (status != null && !status.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("tradeStatus").get("tradeStatus"), status));
        if (fromDate != null) spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("tradeDate"), fromDate));
        if (toDate != null) spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("tradeDate"), toDate));
        return tradeRepository.findAll(spec);
    }

    public Page<Trade> filterTrades(String counterparty, String book, String trader, String status, int page, int size, String sort) {
        Sort order = Sort.by(Sort.Order.desc("tradeDate"));
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            if (parts.length == 2) order = "asc".equalsIgnoreCase(parts[1]) ? Sort.by(Sort.Order.asc(parts[0])) : Sort.by(Sort.Order.desc(parts[0]));
        }
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), order);
        Specification<Trade> spec = Specification.where(null);
        if (counterparty != null && !counterparty.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("counterparty").get("name"), counterparty));
        if (book != null && !book.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("book").get("bookName"), book));
        if (trader != null && !trader.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("traderUser").get("loginId"), trader));
        if (status != null && !status.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("tradeStatus").get("tradeStatus"), status));
        return tradeRepository.findAll(spec, pageable);
    }

    /**
     * Minimal RSQL-like stub: supports simple 'field==value' parts separated by ';'
     * For production: integrate rsql-parser + rsql-jpa or convert to a full Specification builder.
     */
    public List<Trade> searchByRsql(String rsqlQuery) {
        if (rsqlQuery == null || rsqlQuery.isBlank()) return Collections.emptyList();
        Specification<Trade> spec = Specification.where(null);
        String[] parts = rsqlQuery.split(";");
        for (String p : parts) {
            p = p.trim();
            if (p.contains("==")) {
                String[] kv = p.split("==");
                if (kv.length != 2) continue;
                String left = kv[0].trim();
                String right = kv[1].trim();
                if ("counterparty.name".equalsIgnoreCase(left)) spec = spec.and((root, q, cb) -> cb.equal(root.get("counterparty").get("name"), right));
                else if ("tradeStatus.tradeStatus".equalsIgnoreCase(left)) spec = spec.and((root, q, cb) -> cb.equal(root.get("tradeStatus").get("tradeStatus"), right));
                else if ("book.bookName".equalsIgnoreCase(left)) spec = spec.and((root, q, cb) -> cb.equal(root.get("book").get("bookName"), right));
            }
        }
        return tradeRepository.findAll(spec);
    }

    public List<Trade> findTradesByTrader(String loginId) {
        Specification<Trade> spec = (root, q, cb) -> cb.equal(root.get("traderUser").get("loginId"), loginId);
        return tradeRepository.findAll(spec);
    }

    public List<Trade> findTradesByBook(Long bookId) {
        Specification<Trade> spec = (root, q, cb) -> cb.equal(root.get("book").get("id"), bookId);
        return tradeRepository.findAll(spec);
    }

    // === DASHBOARD / SUMMARY ===
    public TradeSummaryDTO getPortfolioSummary(Long bookId) {
        List<Trade> trades = (bookId == null) ? tradeRepository.findAll() : findTradesByBook(bookId);
        TradeSummaryDTO dto = new TradeSummaryDTO();

        dto.setTradesByStatus(trades.stream()
                .filter(t -> t.getTradeStatus() != null)
                .collect(Collectors.groupingBy(t -> t.getTradeStatus().getTradeStatus(), Collectors.counting())));

        dto.setTradesByCounterparty(trades.stream()
                .filter(t -> t.getCounterparty() != null)
                .collect(Collectors.groupingBy(t -> t.getCounterparty().getName(), Collectors.counting())));

        dto.setTradesByTradeType(trades.stream()
                .filter(t -> t.getTradeType() != null)
                .collect(Collectors.groupingBy(t -> t.getTradeType().getTradeType(), Collectors.counting())));

        Map<String, BigDecimal> notionalByCurrency = trades.stream()
                .flatMap(t -> (t.getTradeLegs() == null ? Collections.<TradeLeg>emptyList() : t.getTradeLegs()).stream())
                .filter(l -> l.getCurrency() != null && l.getNotional() != null)
                .collect(Collectors.groupingBy(l -> l.getCurrency().getCurrency(),
                        Collectors.reducing(BigDecimal.ZERO, TradeLeg::getNotional, BigDecimal::add)));

        dto.setNotionalByCurrency(notionalByCurrency);
        return dto;
    }

    public DailySummaryDTO getDailySummary(String trader, LocalDate date) {
        List<Trade> trades = tradeRepository.findAll((root, q, cb) -> cb.equal(root.get("tradeDate"), date));
        if (trader != null && !trader.isEmpty()) {
            trades = trades.stream().filter(t -> t.getTraderUser() != null && trader.equals(t.getTraderUser().getLoginId())).collect(Collectors.toList());
        }

        DailySummaryDTO dto = new DailySummaryDTO();
        dto.setDate(date);
        dto.setTradeCount(trades.size());

        BigDecimal totalNotional = trades.stream()
                .flatMap(t -> (t.getTradeLegs() == null ? Collections.<TradeLeg>emptyList() : t.getTradeLegs()).stream())
                .filter(l -> l.getNotional() != null)
                .map(TradeLeg::getNotional)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalNotional(totalNotional);

        Map<String, BigDecimal> notionalByBook = trades.stream()
                .filter(t -> t.getBook() != null)
                .collect(Collectors.groupingBy(t -> t.getBook().getBookName(),
                        Collectors.reducing(BigDecimal.ZERO,
                                t -> (t.getTradeLegs() == null ? Collections.<TradeLeg>emptyList() : t.getTradeLegs()).stream()
                                        .filter(l -> l.getNotional() != null)
                                        .map(TradeLeg::getNotional)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                                BigDecimal::add)));
        dto.setNotionalByBook(notionalByBook);

        Map<String, Long> tradesByTrader = trades.stream()
                .filter(t -> t.getTraderUser() != null)
                .collect(Collectors.groupingBy(t -> t.getTraderUser().getLoginId(), Collectors.counting()));
        dto.setTradesByTrader(tradesByTrader);

        return dto;
    }
}
