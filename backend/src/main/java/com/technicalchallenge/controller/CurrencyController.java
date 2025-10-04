package com.technicalchallenge.controller;

import com.technicalchallenge.dto.CurrencyDTO;
import com.technicalchallenge.mapper.CurrencyMapper;
import com.technicalchallenge.model.Currency;
import com.technicalchallenge.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController { // Purpose of this controller - to manage currencies, including creating, retrieving, updating, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    // Service to handle business logic
    @Autowired
    private CurrencyService currencyService;

    // Mapper to convert between entity and DTO
    @Autowired
    private CurrencyMapper currencyMapper;

    // Endpoint to retrieve all currencies
    @GetMapping
    public List<CurrencyDTO> getAll() {
        logger.info("Fetching all currencies");
        return currencyService.findAll().stream()
                .map(currencyMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve a currency by its ID
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching currency by id: {}", id);
        return currencyService.findById(id)
                .map(currencyMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Endpoint to create a new currency
    @PostMapping
    public CurrencyDTO create(@RequestBody CurrencyDTO currencyDTO) {
        logger.info("Creating new currency: {}", currencyDTO);
        Currency entity = currencyMapper.toEntity(currencyDTO);
        return currencyMapper.toDto(currencyService.save(entity));
    }
    
    // Endpoint to update an existing currency
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDTO> update(@PathVariable Long id, @RequestBody CurrencyDTO currencyDTO) {
        return currencyService.findById(id)
                .map(existing -> {
                    Currency entity = currencyMapper.toEntity(currencyDTO);
                    entity.setId(id);
                    return ResponseEntity.ok(currencyMapper.toDto(currencyService.save(entity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to delete a currency by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting currency with id: {}", id);
        if (currencyService.findById(id).isPresent()) {
            currencyService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // Endpoint to retrieve all currency values
    @GetMapping("/values")
    public List<String> getAllCurrencyValues() {
        logger.info("Fetching all currency values");
        return currencyService.findAll().stream()
                .map(Currency::getCurrency)
                .toList();
    }
}
