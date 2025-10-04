package com.technicalchallenge.controller;

import com.technicalchallenge.model.BusinessDayConvention;
import com.technicalchallenge.service.BusinessDayConventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/businessDayConventions")
public class BusinessDayConventionController {// Purpose of this controller - to manage business day conventions, including creating, retrieving, updating, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(BusinessDayConventionController.class);

    @Autowired // Injecting BusinessDayConventionService to handle business logic
    private BusinessDayConventionService businessDayConventionService;

    @GetMapping // Endpoint to retrieve all business day conventions
    public List<BusinessDayConvention> getAll() {
        logger.info("Fetching all business day conventions");
        return businessDayConventionService.findAll();
    }

    @GetMapping("/{id}") // Endpoint to retrieve a business day convention by its ID
    public ResponseEntity<BusinessDayConvention> getById(@PathVariable Long id) {
        logger.debug("Fetching business day convention by id: {}", id);
        return businessDayConventionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // Endpoint to create a new business day convention
    public BusinessDayConvention create(@RequestBody BusinessDayConvention businessDayConvention) {
        logger.info("Creating new business day convention: {}", businessDayConvention);
        return businessDayConventionService.save(businessDayConvention);
    }

    @PutMapping("/{id}")// Endpoint to update an existing business day convention
    public ResponseEntity<BusinessDayConvention> update(@PathVariable Long id, @RequestBody BusinessDayConvention businessDayConvention) {
        return businessDayConventionService.findById(id)
                .map(existing -> {
                    businessDayConvention.setId(id);
                    return ResponseEntity.ok(businessDayConventionService.save(businessDayConvention));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // Endpoint to delete a business day convention by its ID
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting business day convention with id: {}", id);
        if (businessDayConventionService.findById(id).isPresent()) {
            businessDayConventionService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/values") // Endpoint to retrieve all business day convention values
    public List<String> getAllBusinessDayConventionValues() {
        logger.info("Fetching all business day convention values");
        return businessDayConventionService.findAll().stream()
                .map(BusinessDayConvention::getBdc)
                .toList();
    }
}
