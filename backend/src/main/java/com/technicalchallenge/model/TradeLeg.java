package com.technicalchallenge.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trade_leg")
public class TradeLeg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long legId;

    private BigDecimal notional;
    private Double rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_id", referencedColumnName = "id")
    private Trade trade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leg_rate_type_id", referencedColumnName = "id")
    private LegType legRateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_id", referencedColumnName = "id")
    private Index index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holiday_calendar_id", referencedColumnName = "id")
    private HolidayCalendar holidayCalendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_period_schedule_id", referencedColumnName = "id")
    private Schedule calculationPeriodSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_business_day_convention_id", referencedColumnName = "id")
    private BusinessDayConvention paymentBusinessDayConvention;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixing_business_day_convention_id", referencedColumnName = "id")
    private BusinessDayConvention fixingBusinessDayConvention;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_rec_id", referencedColumnName = "id")
    private PayRec payReceiveFlag;

    // Audit fields
    private Boolean active = true;
    private LocalDateTime createdDate;
    private LocalDateTime deactivatedDate;

    @OneToMany(mappedBy = "tradeLeg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cashflow> cashflows;
}
