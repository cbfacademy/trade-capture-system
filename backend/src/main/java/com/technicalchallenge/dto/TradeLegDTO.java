package com.technicalchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeLegDTO {
    private Long legId;

    @NotNull(message = "Notional is required")
    @Positive(message = "Notional must be positive")
    private BigDecimal notional;

    private Double rate;

    // Currency reference
    private Long currencyId;
    private String currency;

    // Leg type reference
    private Long legTypeId;
    private String legType;

    // Index reference (for floating legs)
    private Long indexId;
    @JsonProperty("index")
    private String indexName;

    // Holiday calendar reference
    private Long holidayCalendarId;
    private String holidayCalendar;

    // Schedule reference
    private Long scheduleId;
    private String calculationPeriodSchedule;

    // Business day convention references
    private Long paymentBdcId;
    private String paymentBusinessDayConvention;
    private Long fixingBdcId;
    private String fixingBusinessDayConvention;

    // Pay/Receive reference
    private Long payRecId;
    private String payReceiveFlag;

    // Associated cashflows
    private List<CashflowDTO> cashflows;
}
