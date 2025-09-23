package com.technicalchallenge.controller;

import com.technicalchallenge.dto.CashflowDTO;
import com.technicalchallenge.dto.CashflowGenerationRequest;
import com.technicalchallenge.mapper.CashflowMapper;
import com.technicalchallenge.model.Cashflow;
import com.technicalchallenge.service.CashflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/cashflows")
@Validated
public class CashflowController {
    private static final Logger logger = LoggerFactory.getLogger(CashflowController.class);

    @Autowired
    private CashflowService cashflowService;
    @Autowired
    private CashflowMapper cashflowMapper;

    @GetMapping
    public List<CashflowDTO> getAllCashflows() {
        logger.info("Fetching all cashflows");
        return cashflowService.getAllCashflows().stream()
                .map(cashflowMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashflowDTO> getCashflowById(@PathVariable(name = "id") Long id) {
        logger.debug("Fetching cashflow by id: {}", id);
        return cashflowService.getCashflowById(id)
                .map(cashflowMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
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
    public ResponseEntity<Void> deleteCashflow(@PathVariable(name = "id") Long id) {
        logger.warn("Deleting cashflow with id: {}", id);
        cashflowService.deleteCashflow(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate")
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
                    paymentValue = leg.getNotional().multiply(BigDecimal.valueOf(rate)).multiply(BigDecimal.valueOf(days)).divide(BigDecimal.valueOf(360), 2, BigDecimal.ROUND_HALF_UP);
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
