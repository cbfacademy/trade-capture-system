package com.technicalchallenge.dto;

import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CashflowGenerationRequest {
    private List<TradeLegDTO> legs;
    private LocalDate tradeStartDate;
    private LocalDate tradeMaturityDate;

    @Data
    public static class TradeLegDTO {
        private String legType; // "Fixed" or "Floating"
        private BigDecimal notional;
        private Double rate; // for fixed
        private String index; // for floating
        private String calculationPeriodSchedule; // e.g. "Monthly", "Quarterly", "Annually"
        private String paymentBusinessDayConvention;
        private String payReceiveFlag; // "Pay" or "Rec"
    }
}
