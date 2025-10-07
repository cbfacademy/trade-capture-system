package com.technicalchallenge.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cashflow")
public class Cashflow {// Entity to represent a Cashflow
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Changed from 'id' to match DTO

    // Changed field names to match DTO expectations
    private BigDecimal paymentValue;
    private LocalDate valueDate;
    private Double rate;

    // Changed relationships to match DTO expectations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leg_id", referencedColumnName = "legId")
    private TradeLeg tradeLeg; // Changed from 'leg' to match service expectations

    // Changed relationships to match DTO expectations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_rec_id", referencedColumnName = "id")
    private PayRec payRec;

    // Changed relationships to match DTO expectations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id", referencedColumnName = "id")
    private LegType paymentType;

    // Changed relationships to match DTO expectations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_business_day_convention_id", referencedColumnName = "id")
    private BusinessDayConvention paymentBusinessDayConvention;

    // Fixed audit fields to match DTO expectations
    private Boolean active = true;
    private LocalDateTime createdDate;
    private LocalDate validityStartDate; // Changed to LocalDate
    private LocalDate validityEndDate; // Changed to LocalDate
}
