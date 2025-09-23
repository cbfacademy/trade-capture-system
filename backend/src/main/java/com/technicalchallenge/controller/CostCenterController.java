package com.technicalchallenge.controller;

import com.technicalchallenge.dto.CostCenterDTO;
import com.technicalchallenge.mapper.CostCenterMapper;
import com.technicalchallenge.model.CostCenter;
import com.technicalchallenge.service.CostCenterService;
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
@RequestMapping("/api/costCenters")
@Validated
public class CostCenterController {
    private static final Logger logger = LoggerFactory.getLogger(CostCenterController.class);

    @Autowired
    private CostCenterService costCenterService;
    @Autowired
    private CostCenterMapper costCenterMapper;

    @GetMapping
    public List<CostCenterDTO> getAllCostCenters() {
        logger.info("Fetching all cost centers");
        return costCenterService.getAllCostCenters().stream()
                .map(costCenterMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostCenterDTO> getCostCenterById(@PathVariable Long id) {
        logger.debug("Fetching cost center by id: {}", id);
        return costCenterService.getCostCenterById(id)
                .map(costCenterMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCostCenter(@Valid @RequestBody CostCenterDTO costCenterDTO) {
        logger.info("Creating new cost center: {}", costCenterDTO);
        if (costCenterDTO.getCostCenterName() == null || costCenterDTO.getCostCenterName().isBlank()) {
            return ResponseEntity.badRequest().body("Cost center name is required");
        }
        if (costCenterDTO.getSubDeskName() == null) {
            return ResponseEntity.badRequest().body("Sub desk is required");
        }
        var entity = costCenterMapper.toEntity(costCenterDTO);
        var saved = costCenterService.saveCostCenter(entity, costCenterDTO);
        return ResponseEntity.ok(costCenterMapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostCenter(@PathVariable Long id) {
        logger.warn("Deleting cost center with id: {}", id);
        costCenterService.deleteCostCenter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/values")
    public List<String> getAllCostCenterNames() {
        return costCenterService.getAllCostCenters().stream()
                .map(CostCenter::getCostCenterName)
                .toList();
    }
}
