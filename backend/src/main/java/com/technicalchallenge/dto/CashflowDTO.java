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
public class CashflowDTO {
    private Long id;
    private Long legId;
    private BigDecimal paymentValue;
    private LocalDate valueDate;
    private Double rate;
    private String payRec;
    private String paymentType;
    private String paymentBusinessDayConvention;
    private LocalDateTime createdDate;
    private Boolean active;
}
