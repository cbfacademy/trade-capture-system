package com.technicalchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeBlotterDTO {
    private Long tradeId;
    private Integer version;
    private String tradeStatus;
    private LocalDate tradeDate;
    private LocalDate tradeStartDate;
    private LocalDate tradeMaturityDate;
    
    // Reference data
    private String counterpartyName;
    private String bookName;
    private String traderUserName;
    private String inputterUserName;
    private String tradeType;
    private String tradeSubType;
    
    // Trade leg summary
    private BigDecimal leg1Notional;
    private String leg1Currency;
    private String leg1Type;
    private Double leg1Rate;
    
    private BigDecimal leg2Notional;
    private String leg2Currency;
    private String leg2Type;
    private Double leg2Rate;
    
    // Audit fields
    private LocalDateTime createdDate;
    private LocalDateTime lastTouchTimestamp;
    private Boolean active;
    
    // Calculated fields
    private BigDecimal totalNotional; // Sum of both legs
    private String primaryCurrency; // Currency of larger notional
    private Long cashflowCount;
}