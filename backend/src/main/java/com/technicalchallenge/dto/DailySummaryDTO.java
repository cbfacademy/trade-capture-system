package com.technicalchallenge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

// NEW FILE: Daily summary DTO for trader daily stats
public class DailySummaryDTO {
    private LocalDate date;
    private long tradeCount;
    private BigDecimal totalNotional;
    private Map<String, BigDecimal> notionalByBook;
    private Map<String, Long> tradesByTrader;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public long getTradeCount() { return tradeCount; }
    public void setTradeCount(long tradeCount) { this.tradeCount = tradeCount; }

    public BigDecimal getTotalNotional() { return totalNotional; }
    public void setTotalNotional(BigDecimal totalNotional) { this.totalNotional = totalNotional; }

    public Map<String, BigDecimal> getNotionalByBook() { return notionalByBook; }
    public void setNotionalByBook(Map<String, BigDecimal> notionalByBook) { this.notionalByBook = notionalByBook; }

    public Map<String, Long> getTradesByTrader() { return tradesByTrader; }
    public void setTradesByTrader(Map<String, Long> tradesByTrader) { this.tradesByTrader = tradesByTrader; }
}
