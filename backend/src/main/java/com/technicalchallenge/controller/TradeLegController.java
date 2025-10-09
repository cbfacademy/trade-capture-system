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

    // Endpoint to delete a trade leg by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTradeLeg(@PathVariable(name = "id") Long id) {
        logger.warn("Deleting trade leg with id: {}", id);
        tradeLegService.deleteTradeLeg(id);
        return ResponseEntity.noContent().build();
    }
}

/*Developer Notes:
 * fix(test): TradeControllerTest & TradeLegControllerTest - align controller validation & responses

- Problem: Controller tests were failing due to inconsistent status codes and missing/incorrect validation messages 
(e.g. missing trade date, missing book/counterparty, delete response code, update-ID mismatch). 
- Root Cause: Controller endpoints returned generic or inconsistent HTTP responses 
and did not perform explicit request-level validation expected by the tests.
- Solution: Added explicit request validations and consistent response codes/messages:
  - POST /api/trades: validate required fields and return 400 with explicit messages when missing; return 201 on success.
  - PUT /api/trades/{id}: validate path/body ID consistency and return 400 when mismatched.
  - DELETE /api/trades/{id}: return 204 No Content on successful deletion.
  - TradeLeg POST -> existing notional validation retained (returns explicit "Notional must be positive").
- Impact: Controller endpoints behave deterministically for tests and API clients; 
fixes tests and improves API error messages. Verified with `mvn -Dtest=TradeControllerTest,TradeLegControllerTest test` (green).
 */