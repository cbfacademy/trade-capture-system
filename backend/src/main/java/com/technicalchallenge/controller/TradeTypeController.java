package com.technicalchallenge.controller;

import com.technicalchallenge.dto.TradeTypeDTO;
import com.technicalchallenge.mapper.TradeTypeMapper;
import com.technicalchallenge.model.TradeType;
import com.technicalchallenge.service.TradeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/tradeTypes")
public class TradeTypeController {// Purpose of this controller - to manage trade types, including creating, retrieving, updating, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(TradeTypeController.class);

    // Injecting TradeTypeService to handle business logic
    @Autowired
    private TradeTypeService tradeTypeService;

    // Mapper to convert between entity and DTO
    @Autowired
    private TradeTypeMapper tradeTypeMapper;

    // Endpoint to retrieve all trade types
    @GetMapping
    public List<TradeTypeDTO> getAll() {
        logger.info("Fetching all trade types");
        return tradeTypeService.findAll().stream()
                .map(tradeTypeMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve a trade type by its ID
    @GetMapping("/{id}")
    public ResponseEntity<TradeTypeDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching trade type by id: {}", id);
        return tradeTypeService.findById(id)
                .map(tradeTypeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a new trade type
    @PostMapping
    public TradeTypeDTO create(@RequestBody TradeTypeDTO tradeTypeDTO) {
        logger.info("Creating new trade type: {}", tradeTypeDTO);
        TradeType entity = tradeTypeMapper.toEntity(tradeTypeDTO);
        return tradeTypeMapper.toDto(tradeTypeService.save(entity));
    }

    // Endpoint to update an existing trade type
    @PutMapping("/{id}")
    public ResponseEntity<TradeTypeDTO> update(@PathVariable Long id, @RequestBody TradeTypeDTO tradeTypeDTO) {
        return tradeTypeService.findById(id)
                .map(existing -> {
                    TradeType entity = tradeTypeMapper.toEntity(tradeTypeDTO);
                    entity.setId(id);
                    return ResponseEntity.ok(tradeTypeMapper.toDto(tradeTypeService.save(entity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to delete a trade type by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting trade type with id: {}", id);
        if (tradeTypeService.findById(id).isPresent()) {
            tradeTypeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to retrieve all trade type values
    @GetMapping("/values")
    public List<String> getAllTradeTypeValues() {
        logger.info("Fetching all trade type values");
        return tradeTypeService.findAll().stream()
                .map(TradeType::getTradeType)
                .toList();
    }
}
