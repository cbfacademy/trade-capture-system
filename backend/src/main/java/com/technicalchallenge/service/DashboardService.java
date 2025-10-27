package com.technicalchallenge.service;

import com.technicalchallenge.dto.DashboardSummaryDTO;
import com.technicalchallenge.dto.TradeBlotterDTO;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.repository.TradeRepository;
import com.technicalchallenge.repository.CashflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    @Autowired
    private TradeRepository tradeRepository;
    
    @Autowired
    private CashflowRepository cashflowRepository;

    /**
     * Get dashboard summary for all users or specific trader
     */
    public DashboardSummaryDTO getDashboardSummary(Long userId) {
        logger.info("Generating dashboard summary for user: {}", userId);
        
        List<Trade> trades;
        if (userId != null) {
            trades = tradeRepository.findByTraderUserId(userId);
        } else {
            trades = tradeRepository.findAll().stream()
                .filter(Trade::getActive)
                .collect(Collectors.toList());
        }

        DashboardSummaryDTO summary = new DashboardSummaryDTO();
        
        // Basic counts
        summary.setTotalTrades((long) trades.size());
        summary.setActiveTrades(trades.stream()
            .filter(Trade::getActive)
            .count());
        
        // Status counts
        Map<String, Long> statusCounts = trades.stream()
            .filter(Trade::getActive)
            .collect(Collectors.groupingBy(
                t -> t.getTradeStatus() != null ? t.getTradeStatus().getTradeStatus() : "UNKNOWN",
                Collectors.counting()
            ));
        
        summary.setNewTrades(statusCounts.getOrDefault("NEW", 0L));
        summary.setAmendedTrades(statusCounts.getOrDefault("AMENDED", 0L));
        summary.setTerminatedTrades(statusCounts.getOrDefault("TERMINATED", 0L));
        
        // Notional calculations
        BigDecimal totalNotional = calculateTotalNotional(trades);
        summary.setTotalNotional(totalNotional);
        
        // Date-based metrics
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.minusDays(30);
        
        summary.setTradesToday(trades.stream()
            .filter(t -> t.getTradeDate() != null && t.getTradeDate().equals(today))
            .count());
            
        summary.setTradesThisWeek(trades.stream()
            .filter(t -> t.getTradeDate() != null && !t.getTradeDate().isBefore(weekStart))
            .count());
            
        summary.setTradesThisMonth(trades.stream()
            .filter(t -> t.getTradeDate() != null && !t.getTradeDate().isBefore(monthStart))
            .count());
        
        // Notional by period
        summary.setNotionalToday(calculateNotionalForPeriod(trades, today, today));
        summary.setNotionalThisWeek(calculateNotionalForPeriod(trades, weekStart, today));
        summary.setNotionalThisMonth(calculateNotionalForPeriod(trades, monthStart, today));
        
        // Most active entities
        summary.setMostActiveCounterparty(findMostActiveCounterparty(trades));
        summary.setMostActiveBook(findMostActiveBook(trades));
        
        // Latest trade date
        summary.setLastTradeDate(trades.stream()
            .filter(t -> t.getTradeDate() != null)
            .map(Trade::getTradeDate)
            .max(LocalDate::compareTo)
            .orElse(null));
        
        logger.info("Dashboard summary generated with {} total trades", summary.getTotalTrades());
        return summary;
    }

    /**
     * Get trade blotter with enhanced information
     */
    public Page<TradeBlotterDTO> getTradeBlotter(Long userId, Pageable pageable) {
        logger.info("Getting trade blotter for user: {} with pagination: {}", userId, pageable);
        
        Page<Trade> tradesPage;
        if (userId != null) {
            // Get trades for specific trader
            List<Trade> userTrades = tradeRepository.findByTraderUserId(userId);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), userTrades.size());
            List<Trade> pageContent = userTrades.subList(start, end);
            tradesPage = new PageImpl<>(pageContent, pageable, userTrades.size());
        } else {
            // Get all active trades with pagination
            tradesPage = tradeRepository.findAllActivePaginated(pageable);
        }
        
        List<TradeBlotterDTO> blotterDTOs = tradesPage.getContent().stream()
            .map(this::convertToBlotterDTO)
            .collect(Collectors.toList());
        
        return new PageImpl<>(blotterDTOs, pageable, tradesPage.getTotalElements());
    }

    /**
     * Get trader-specific blotter
     */
    public List<TradeBlotterDTO> getTraderBlotter(Long traderId) {
        logger.info("Getting trader blotter for trader ID: {}", traderId);
        
        List<Trade> trades = tradeRepository.findByTraderUserId(traderId);
        return trades.stream()
            .map(this::convertToBlotterDTO)
            .collect(Collectors.toList());
    }

    /**
     * Convert Trade entity to TradeBlotterDTO
     */
    private TradeBlotterDTO convertToBlotterDTO(Trade trade) {
        TradeBlotterDTO dto = new TradeBlotterDTO();
        
        // Basic trade info
        dto.setTradeId(trade.getTradeId());
        dto.setVersion(trade.getVersion());
        dto.setTradeDate(trade.getTradeDate());
        dto.setTradeStartDate(trade.getTradeStartDate());
        dto.setTradeMaturityDate(trade.getTradeMaturityDate());
        dto.setActive(trade.getActive());
        dto.setCreatedDate(trade.getCreatedDate());
        dto.setLastTouchTimestamp(trade.getLastTouchTimestamp());
        
        // Reference data
        dto.setTradeStatus(trade.getTradeStatus() != null ? trade.getTradeStatus().getTradeStatus() : null);
        dto.setCounterpartyName(trade.getCounterparty() != null ? trade.getCounterparty().getName() : null);
        dto.setBookName(trade.getBook() != null ? trade.getBook().getBookName() : null);
        dto.setTraderUserName(trade.getTraderUser() != null ? 
            trade.getTraderUser().getFirstName() + " " + trade.getTraderUser().getLastName() : null);
        dto.setInputterUserName(trade.getTradeInputterUser() != null ? 
            trade.getTradeInputterUser().getFirstName() + " " + trade.getTradeInputterUser().getLastName() : null);
        dto.setTradeType(trade.getTradeType() != null ? trade.getTradeType().getTradeType() : null);
        dto.setTradeSubType(trade.getTradeSubType() != null ? trade.getTradeSubType().getTradeSubType() : null);
        
        // Trade legs info
        if (trade.getTradeLegs() != null && !trade.getTradeLegs().isEmpty()) {
            List<TradeLeg> legs = trade.getTradeLegs();
            
            if (legs.size() >= 1) {
                TradeLeg leg1 = legs.get(0);
                dto.setLeg1Notional(leg1.getNotional());
                dto.setLeg1Currency(leg1.getCurrency() != null ? leg1.getCurrency().getCurrency() : null);
                dto.setLeg1Type(leg1.getLegRateType() != null ? leg1.getLegRateType().getType() : null);
                dto.setLeg1Rate(leg1.getRate());
            }
            
            if (legs.size() >= 2) {
                TradeLeg leg2 = legs.get(1);
                dto.setLeg2Notional(leg2.getNotional());
                dto.setLeg2Currency(leg2.getCurrency() != null ? leg2.getCurrency().getCurrency() : null);
                dto.setLeg2Type(leg2.getLegRateType() != null ? leg2.getLegRateType().getType() : null);
                dto.setLeg2Rate(leg2.getRate());
            }
            
            // Calculate total notional and primary currency
            BigDecimal totalNotional = legs.stream()
                .map(TradeLeg::getNotional)
                .filter(notional -> notional != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotalNotional(totalNotional);
            
            // Determine primary currency (currency of larger notional)
            if (legs.size() >= 2 && dto.getLeg1Notional() != null && dto.getLeg2Notional() != null) {
                if (dto.getLeg1Notional().compareTo(dto.getLeg2Notional()) >= 0) {
                    dto.setPrimaryCurrency(dto.getLeg1Currency());
                } else {
                    dto.setPrimaryCurrency(dto.getLeg2Currency());
                }
            } else if (legs.size() >= 1) {
                dto.setPrimaryCurrency(dto.getLeg1Currency());
            }
        }
        
        // Cashflow count
        if (trade.getTradeId() != null) {
            dto.setCashflowCount(cashflowRepository.countByTradeLegTradeTradeId(trade.getTradeId()));
        }
        
        return dto;
    }

    private BigDecimal calculateTotalNotional(List<Trade> trades) {
        return trades.stream()
            .filter(Trade::getActive)
            .flatMap(trade -> trade.getTradeLegs() != null ? trade.getTradeLegs().stream() : null)
            .filter(leg -> leg != null && leg.getNotional() != null)
            .map(TradeLeg::getNotional)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateNotionalForPeriod(List<Trade> trades, LocalDate startDate, LocalDate endDate) {
        return trades.stream()
            .filter(Trade::getActive)
            .filter(t -> t.getTradeDate() != null && 
                        !t.getTradeDate().isBefore(startDate) && 
                        !t.getTradeDate().isAfter(endDate))
            .flatMap(trade -> trade.getTradeLegs() != null ? trade.getTradeLegs().stream() : null)
            .filter(leg -> leg != null && leg.getNotional() != null)
            .map(TradeLeg::getNotional)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String findMostActiveCounterparty(List<Trade> trades) {
        return trades.stream()
            .filter(Trade::getActive)
            .filter(t -> t.getCounterparty() != null)
            .collect(Collectors.groupingBy(
                t -> t.getCounterparty().getName(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    private String findMostActiveBook(List<Trade> trades) {
        return trades.stream()
            .filter(Trade::getActive)
            .filter(t -> t.getBook() != null)
            .collect(Collectors.groupingBy(
                t -> t.getBook().getBookName(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
}