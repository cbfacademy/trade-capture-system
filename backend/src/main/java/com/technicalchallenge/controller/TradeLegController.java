package com.technicalchallenge.controller;

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

import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.mapper.TradeLegMapper;
import com.technicalchallenge.service.TradeLegService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tradeLegs")
@Validated
@Tag(name = "Trade Legs", description = "Trade leg operations for managing individual legs of complex trades")
public class TradeLegController {
    private static final Logger logger = LoggerFactory.getLogger(TradeLegController.class);

    @Autowired
    private TradeLegService tradeLegService;
    @Autowired
    private TradeLegMapper tradeLegMapper;

    @GetMapping
    public List<TradeLegDTO> getAllTradeLegs() {
        logger.info("Fetching all trade legs");
        return tradeLegService.getAllTradeLegs().stream()
                .map(tradeLegMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeLegDTO> getTradeLegById(@PathVariable Long id) {
        logger.debug("Fetching trade leg by id: {}", id);
        return tradeLegService.getTradeLegById(id)
                .map(tradeLegMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTradeLeg(@Valid @RequestBody TradeLegDTO tradeLegDTO) {
        logger.info("Creating new trade leg: {}", tradeLegDTO);
        // Validation: notional > 0, trade, currency, legRateType required
        if (tradeLegDTO.getNotional() == null || tradeLegDTO.getNotional().signum() <= 0) {
            return ResponseEntity.badRequest().body("Notional must be positive");
        }
        if (tradeLegDTO.getCurrency() == null || tradeLegDTO.getLegType() == null) {
            return ResponseEntity.badRequest().body("Currency and Leg Rate Type are required");
        }
        var entity = tradeLegMapper.toEntity(tradeLegDTO);
        var saved = tradeLegService.saveTradeLeg(entity, tradeLegDTO);
        return ResponseEntity.ok(tradeLegMapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTradeLeg(@PathVariable Long id) {
        logger.warn("Deleting trade leg with id: {}", id);
        tradeLegService.deleteTradeLeg(id);
        return ResponseEntity.noContent().build();
    }
}
