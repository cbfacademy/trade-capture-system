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
public class TradeLegController {// Purpose of this controller - to manage trade legs, including creating, retrieving, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(TradeLegController.class);

    // Injecting TradeLegService to handle business logic
    @Autowired
    private TradeLegService tradeLegService;

    // Mapper to convert between entity and DTO
    @Autowired
    private TradeLegMapper tradeLegMapper;

    // Endpoint to retrieve all trade legs
    @GetMapping
    public List<TradeLegDTO> getAllTradeLegs() {
        logger.info("Fetching all trade legs");
        return tradeLegService.getAllTradeLegs().stream()
                .map(tradeLegMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve a trade leg by its ID
    @GetMapping("/{id}")
    public ResponseEntity<TradeLegDTO> getTradeLegById(@PathVariable(name = "id") Long id) {
        logger.debug("Fetching trade leg by id: {}", id);
        return tradeLegService.getTradeLegById(id)
                .map(tradeLegMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a new trade leg
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

    // Endpoint to delete a trade leg by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTradeLeg(@PathVariable(name = "id") Long id) {
        logger.warn("Deleting trade leg with id: {}", id);
        tradeLegService.deleteTradeLeg(id);
        return ResponseEntity.noContent().build();
    }
}
