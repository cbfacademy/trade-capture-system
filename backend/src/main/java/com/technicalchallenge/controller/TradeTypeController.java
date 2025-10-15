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

import com.technicalchallenge.dto.TradeTypeDTO;
import com.technicalchallenge.mapper.TradeTypeMapper;
import com.technicalchallenge.model.TradeType;
import com.technicalchallenge.service.TradeTypeService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tradeTypes")
@Tag(name = "Trade Types", description = "Trade type reference data and classification management")
public class TradeTypeController {
    private static final Logger logger = LoggerFactory.getLogger(TradeTypeController.class);

    @Autowired
    private TradeTypeService tradeTypeService;

    @Autowired
    private TradeTypeMapper tradeTypeMapper;

    @GetMapping
    public List<TradeTypeDTO> getAll() {
        logger.info("Fetching all trade types");
        return tradeTypeService.findAll().stream()
                .map(tradeTypeMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeTypeDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching trade type by id: {}", id);
        return tradeTypeService.findById(id)
                .map(tradeTypeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TradeTypeDTO create(@RequestBody TradeTypeDTO tradeTypeDTO) {
        logger.info("Creating new trade type: {}", tradeTypeDTO);
        TradeType entity = tradeTypeMapper.toEntity(tradeTypeDTO);
        return tradeTypeMapper.toDto(tradeTypeService.save(entity));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting trade type with id: {}", id);
        if (tradeTypeService.findById(id).isPresent()) {
            tradeTypeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/values")
    public List<String> getAllTradeTypeValues() {
        logger.info("Fetching all trade type values");
        return tradeTypeService.findAll().stream()
                .map(TradeType::getTradeType)
                .toList();
    }
}
