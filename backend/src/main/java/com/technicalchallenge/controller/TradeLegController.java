package com.technicalchallenge.controller;

import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.mapper.TradeLegMapper;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.service.TradeLegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/tradeLegs")
@Validated
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
    public ResponseEntity<TradeLegDTO> getTradeLegById(@PathVariable(name = "id") Long id) {
        logger.debug("Fetching trade leg by id: {}", id);
        return tradeLegService.getTradeLegById(id)
                .map(tradeLegMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTradeLeg(@RequestBody TradeLegDTO tradeLegDTO) {
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
    public ResponseEntity<Void> deleteTradeLeg(@PathVariable(name = "id") Long id) {
        logger.warn("Deleting trade leg with id: {}", id);
        tradeLegService.deleteTradeLeg(id);
        return ResponseEntity.noContent().build();
    }
}
