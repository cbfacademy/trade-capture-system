package com.technicalchallenge.controller;

import com.technicalchallenge.dto.LegTypeDTO;
import com.technicalchallenge.mapper.LegTypeMapper;
import com.technicalchallenge.model.LegType;
import com.technicalchallenge.service.LegTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/legTypes")
public class LegTypeController {
    private static final Logger logger = LoggerFactory.getLogger(LegTypeController.class);

    @Autowired
    private LegTypeService legTypeService;

    @Autowired
    private LegTypeMapper legTypeMapper;

    @GetMapping
    public List<LegTypeDTO> getAll() {
        logger.info("Fetching all leg types");
        return legTypeService.findAll().stream()
                .map(legTypeMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LegTypeDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching leg type by id: {}", id);
        return legTypeService.findById(id)
                .map(legTypeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LegTypeDTO create(@RequestBody LegTypeDTO legTypeDTO) {
        logger.info("Creating new leg type: {}", legTypeDTO);
        LegType entity = legTypeMapper.toEntity(legTypeDTO);
        return legTypeMapper.toDto(legTypeService.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LegTypeDTO> update(@PathVariable Long id, @RequestBody LegTypeDTO legTypeDTO) {
        return legTypeService.findById(id)
                .map(existing -> {
                    LegType entity = legTypeMapper.toEntity(legTypeDTO);
                    entity.setId(id);
                    return ResponseEntity.ok(legTypeMapper.toDto(legTypeService.save(entity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting leg type with id: {}", id);
        if (legTypeService.findById(id).isPresent()) {
            legTypeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/values")
    public List<String> getAllLegTypeValues() {
        logger.info("Fetching all leg type values");
        return legTypeService.findAll().stream()
                .map(LegType::getType)
                .toList();
    }
}
