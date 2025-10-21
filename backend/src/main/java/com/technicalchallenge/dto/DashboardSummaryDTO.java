package com.technicalchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private Long totalTrades;
    private Long activeTrades;
    private Long newTrades;
    private Long amendedTrades;
    private Long terminatedTrades;
    private BigDecimal totalNotional;
    private LocalDate lastTradeDate;
    private String mostActiveCounterparty;
    private String mostActiveBook;
    
    // Period-specific metrics
    private Long tradesToday;
    private Long tradesThisWeek;
    private Long tradesThisMonth;
    private BigDecimal notionalToday;
    private BigDecimal notionalThisWeek;
    private BigDecimal notionalThisMonth;
}