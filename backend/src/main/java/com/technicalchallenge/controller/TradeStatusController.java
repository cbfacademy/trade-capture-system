package com.technicalchallenge.controller;

import com.technicalchallenge.dto.TradeStatusDTO;
import com.technicalchallenge.mapper.TradeStatusMapper;
import com.technicalchallenge.model.TradeStatus;
import com.technicalchallenge.service.TradeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/tradeStatus")
public class TradeStatusController {// Purpose of this controller - to manage trade statuses, including creating, retrieving, updating, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(TradeStatusController.class);

    // Injecting TradeStatusService to handle business logic
    @Autowired
    private TradeStatusService tradeStatusService;

    // Mapper to convert between entity and DTO
    @Autowired
    private TradeStatusMapper tradeStatusMapper;

    // Endpoint to retrieve all trade statuses
    @GetMapping
    public List<TradeStatusDTO> getAll() {
        logger.info("Fetching all trade statuses");
        return tradeStatusService.findAll().stream()
                .map(tradeStatusMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve a trade status by its ID
    @GetMapping("/{id}")
    public ResponseEntity<TradeStatusDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching trade status by id: {}", id);
        return tradeStatusService.findById(id)
                .map(tradeStatusMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a new trade status
    @PostMapping
    public TradeStatusDTO create(@RequestBody TradeStatusDTO tradeStatusDTO) {
        logger.info("Creating new trade status: {}", tradeStatusDTO);
        TradeStatus entity = tradeStatusMapper.toEntity(tradeStatusDTO);
        return tradeStatusMapper.toDto(tradeStatusService.save(entity));
    }

    // Endpoint to update an existing trade status
    @PutMapping("/{id}")
    public ResponseEntity<TradeStatusDTO> update(@PathVariable Long id, @RequestBody TradeStatusDTO tradeStatusDTO) {
        return tradeStatusService.findById(id)
                .map(existing -> {
                    TradeStatus entity = tradeStatusMapper.toEntity(tradeStatusDTO);
                    entity.setId(id);
                    return ResponseEntity.ok(tradeStatusMapper.toDto(tradeStatusService.save(entity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to delete a trade status by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting trade status with id: {}", id);
        if (tradeStatusService.findById(id).isPresent()) {
            tradeStatusService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to retrieve all trade status values
    @GetMapping("/values")
    public List<String> getAllTradeStatusValues() {
        logger.info("Fetching all trade status values");
        return tradeStatusService.findAll().stream()
                .map(TradeStatus::getTradeStatus)
                .toList();
    }
}
