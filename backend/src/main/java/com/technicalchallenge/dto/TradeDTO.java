package com.technicalchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {
    private Long id;

    private Long tradeId;

    private Integer version;

    @NotNull(message = "Trade date is required")
    private LocalDate tradeDate;

    @JsonProperty("startDate")
    private LocalDate tradeStartDate;

    @JsonProperty("maturityDate")
    private LocalDate tradeMaturityDate;

    @JsonProperty("executionDate")
    private LocalDate tradeExecutionDate;

    private String utiCode;
    private LocalDateTime lastTouchTimestamp;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private Boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime deactivatedDate;

    // Book reference
    private Long bookId;
    private String bookName;

    // Counterparty reference
    private Long counterpartyId;
    @NotNull(message = "Counterparty name is required")
    private String counterpartyName;

    // User references
    private Long traderUserId;
    private String traderUserName;
    private Long tradeInputterUserId;
    private String inputterUserName;

    // Trade type references
    private Long tradeTypeId;
    private String tradeType;
    private Long tradeSubTypeId;
    private String tradeSubType;

    // Trade status
    private Long tradeStatusId;
    private String tradeStatus;

    // Trade legs
    private List<TradeLegDTO> tradeLegs;

    // Additional fields for extensibility
    private List<AdditionalInfoDTO> additionalFields;
}