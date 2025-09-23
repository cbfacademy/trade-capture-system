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
public class TradeStatusController {
    private static final Logger logger = LoggerFactory.getLogger(TradeStatusController.class);

    @Autowired
    private TradeStatusService tradeStatusService;

    @Autowired
    private TradeStatusMapper tradeStatusMapper;

    @GetMapping
    public List<TradeStatusDTO> getAll() {
        logger.info("Fetching all trade statuses");
        return tradeStatusService.findAll().stream()
                .map(tradeStatusMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeStatusDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching trade status by id: {}", id);
        return tradeStatusService.findById(id)
                .map(tradeStatusMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TradeStatusDTO create(@RequestBody TradeStatusDTO tradeStatusDTO) {
        logger.info("Creating new trade status: {}", tradeStatusDTO);
        TradeStatus entity = tradeStatusMapper.toEntity(tradeStatusDTO);
        return tradeStatusMapper.toDto(tradeStatusService.save(entity));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting trade status with id: {}", id);
        if (tradeStatusService.findById(id).isPresent()) {
            tradeStatusService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/values")
    public List<String> getAllTradeStatusValues() {
        logger.info("Fetching all trade status values");
        return tradeStatusService.findAll().stream()
                .map(TradeStatus::getTradeStatus)
                .toList();
    }
}
