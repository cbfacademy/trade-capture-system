package com.technicalchallenge.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technicalchallenge.model.BusinessDayConvention;
import com.technicalchallenge.service.BusinessDayConventionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/businessDayConventions")
@Tag(name = "Business Day Conventions", description = "Business day convention reference data for date calculations")
public class BusinessDayConventionController {
    private static final Logger logger = LoggerFactory.getLogger(BusinessDayConventionController.class);

    @Autowired
    private BusinessDayConventionService businessDayConventionService;

    @GetMapping
    public List<BusinessDayConvention> getAll() {
        logger.info("Fetching all business day conventions");
        return businessDayConventionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDayConvention> getById(@PathVariable Long id) {
        logger.debug("Fetching business day convention by id: {}", id);
        return businessDayConventionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BusinessDayConvention create(@RequestBody BusinessDayConvention businessDayConvention) {
        logger.info("Creating new business day convention: {}", businessDayConvention);
        return businessDayConventionService.save(businessDayConvention);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessDayConvention> update(@PathVariable Long id, @RequestBody BusinessDayConvention businessDayConvention) {
        return businessDayConventionService.findById(id)
                .map(existing -> {
                    businessDayConvention.setId(id);
                    return ResponseEntity.ok(businessDayConventionService.save(businessDayConvention));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting business day convention with id: {}", id);
        if (businessDayConventionService.findById(id).isPresent()) {
            businessDayConventionService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/values")
    public List<String> getAllBusinessDayConventionValues() {
        logger.info("Fetching all business day convention values");
        return businessDayConventionService.findAll().stream()
                .map(BusinessDayConvention::getBdc)
                .toList();
    }
}
