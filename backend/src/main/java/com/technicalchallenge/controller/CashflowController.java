package com.technicalchallenge.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technicalchallenge.dto.CashflowDTO;
import com.technicalchallenge.dto.CashflowGenerationRequest;
import com.technicalchallenge.mapper.CashflowMapper;
import com.technicalchallenge.service.CashflowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cashflows")
@Validated
@Tag(name = "Cashflows", description = "Cashflow generation and management for trades")
public class CashflowController {
    private static final Logger logger = LoggerFactory.getLogger(CashflowController.class);

    @Autowired
    private CashflowService cashflowService;
    @Autowired
    private CashflowMapper cashflowMapper;

    @GetMapping
    @Operation(summary = "Get all cashflows",
               description = "Retrieves a list of all generated cashflows in the system with payment dates and amounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all cashflows",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = CashflowDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<CashflowDTO> getAllCashflows() {
        logger.info("Fetching all cashflows");
        return cashflowService.getAllCashflows().stream()
                .map(cashflowMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cashflow by ID",
               description = "Retrieves a specific cashflow by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cashflow found and returned successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = CashflowDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cashflow not found"),
        @ApiResponse(responseCode = "400", description = "Invalid cashflow ID format")
    })
    public ResponseEntity<CashflowDTO> getCashflowById(
            @Parameter(description = "Unique identifier of the cashflow", required = true)
            @PathVariable Long id) {
        logger.debug("Fetching cashflow by id: {}", id);
        return cashflowService.getCashflowById(id)
                .map(cashflowMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new cashflow",
               description = "Adds a new cashflow to the system with the specified payment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cashflow created successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = CashflowDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data for cashflow creation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createCashflow(@Valid @RequestBody CashflowDTO cashflowDTO) {
        logger.info("Creating new cashflow: {}", cashflowDTO);
        // Validation: value > 0, valueDate not null
        if (cashflowDTO.getPaymentValue() == null || cashflowDTO.getPaymentValue().signum() <= 0) {
            return ResponseEntity.badRequest().body("Cashflow value must be positive");
        }
        if (cashflowDTO.getValueDate() == null) {
            return ResponseEntity.badRequest().body("Value date is required");
        }
        var entity = cashflowMapper.toEntity(cashflowDTO);
        cashflowService.populateReferenceDataByName(entity, cashflowDTO);
        var saved = cashflowService.saveCashflow(entity);
        return ResponseEntity.ok(cashflowMapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete cashflow",
               description = "Removes a cashflow from the system by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cashflow deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Cashflow not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCashflow(@PathVariable Long id) {
        logger.warn("Deleting cashflow with id: {}", id);
        cashflowService.deleteCashflow(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate cashflows",
               description = "Creates a series of cashflows based on trade legs and specified generation parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cashflows generated successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = CashflowDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data for cashflow generation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CashflowDTO>> generateCashflows(@RequestBody CashflowGenerationRequest request) {
        List<CashflowDTO> allCashflows = new ArrayList<>();
        if (request.getLegs() == null || request.getLegs().isEmpty()) {
            return ResponseEntity.badRequest().body(allCashflows);
        }
        for (CashflowGenerationRequest.TradeLegDTO leg : request.getLegs()) {
            LocalDate startDate = request.getTradeStartDate();
            LocalDate maturityDate = request.getTradeMaturityDate();
            String schedule = leg.getCalculationPeriodSchedule();
            int months = scheduleToMonths(schedule);
            if (months <= 0) {
                continue;
            }
            LocalDate valueDate = startDate;
            while (valueDate.isBefore(maturityDate)) {
                LocalDate nextValueDate = valueDate.plusMonths(months);
                if (nextValueDate.isAfter(maturityDate)) {
                    nextValueDate = maturityDate;
                }
                BigDecimal paymentValue = BigDecimal.ZERO;
                if ("Fixed".equalsIgnoreCase(leg.getLegType())) {
                    long days = java.time.temporal.ChronoUnit.DAYS.between(valueDate, nextValueDate);
                    double rate = leg.getRate() != null ? leg.getRate() : 0.0;
                    paymentValue = leg.getNotional().multiply(BigDecimal.valueOf(rate)).multiply(BigDecimal.valueOf(days)).divide(BigDecimal.valueOf(360), 2, RoundingMode.HALF_UP);
                }
                // For floating, paymentValue remains 0
                CashflowDTO cf = new CashflowDTO();
                cf.setValueDate(nextValueDate);
                cf.setPaymentValue(paymentValue);
                cf.setPayRec(leg.getPayReceiveFlag());
                cf.setPaymentType(leg.getLegType());
                cf.setPaymentBusinessDayConvention(leg.getPaymentBusinessDayConvention());
                cf.setRate(leg.getRate());
                allCashflows.add(cf);
                valueDate = nextValueDate;
            }
        }
        return ResponseEntity.ok(allCashflows);
    }

    private int scheduleToMonths(String schedule) {
        if (schedule == null) return 0;
        schedule = schedule.toLowerCase();
        if (schedule.contains("month")) {
            if (schedule.contains("3")) return 3;
            if (schedule.contains("6")) return 6;
            if (schedule.contains("12")) return 12;
            return 1;
        }
        if (schedule.contains("quarter")) return 3;
        if (schedule.contains("annual") || schedule.contains("year")) return 12;
        return 0;
    }

}
