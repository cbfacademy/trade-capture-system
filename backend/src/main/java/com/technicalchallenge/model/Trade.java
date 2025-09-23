package com.technicalchallenge.model;

import jakarta.persistence.*;
import java.time.LocalDate;
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
@Table(name = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tradeId;
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counterparty_id", referencedColumnName = "id")
    private Counterparty counterparty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trader_user_id", referencedColumnName = "id")
    private ApplicationUser traderUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputter_user_id", referencedColumnName = "id")
    private ApplicationUser tradeInputterUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_type_id", referencedColumnName = "id")
    private TradeType tradeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_sub_type_id", referencedColumnName = "id")
    private TradeSubType tradeSubType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_status_id", referencedColumnName = "id")
    private TradeStatus tradeStatus;

    private String utiCode;

    // Date fields
    private LocalDate tradeDate;
    private LocalDate tradeStartDate;
    private LocalDate tradeMaturityDate;
    private LocalDate tradeExecutionDate;

    private Long additionalFieldsId;
    private LocalDateTime lastTouchTimestamp;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;

    // Audit fields
    private Boolean active = true;
    private LocalDateTime createdDate;
    private LocalDateTime deactivatedDate;

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TradeLeg> tradeLegs;
}
