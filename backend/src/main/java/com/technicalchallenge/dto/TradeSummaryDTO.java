package com.technicalchallenge.dto;

import java.math.BigDecimal;
import java.util.Map;

// NEW FILE: Portfolio summary DTO for blotter/dashboard
public class TradeSummaryDTO {
    private Map<String, Long> tradesByStatus;
    private Map<String, BigDecimal> notionalByCurrency;
    private Map<String, Long> tradesByCounterparty;
    private Map<String, Long> tradesByTradeType;

    public Map<String, Long> getTradesByStatus() { return tradesByStatus; }
    public void setTradesByStatus(Map<String, Long> tradesByStatus) { this.tradesByStatus = tradesByStatus; }

    public Map<String, BigDecimal> getNotionalByCurrency() { return notionalByCurrency; }
    public void setNotionalByCurrency(Map<String, BigDecimal> notionalByCurrency) { this.notionalByCurrency = notionalByCurrency; }

    public Map<String, Long> getTradesByCounterparty() { return tradesByCounterparty; }
    public void setTradesByCounterparty(Map<String, Long> tradesByCounterparty) { this.tradesByCounterparty = tradesByCounterparty; }

    public Map<String, Long> getTradesByTradeType() { return tradesByTradeType; }
    public void setTradesByTradeType(Map<String, Long> tradesByTradeType) { this.tradesByTradeType = tradesByTradeType; }
}

