package com.technicalchallenge.controller;

import com.technicalchallenge.dto.DeskDTO;
import com.technicalchallenge.mapper.DeskMapper;
import com.technicalchallenge.model.Desk;
import com.technicalchallenge.service.DeskService;
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
@RequestMapping("/api/desks")
@Validated
public class DeskController {// Purpose of this controller - to manage desks, including creating, retrieving, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(DeskController.class);
    
    // Injecting DeskService to handle business logic
    @Autowired
    private DeskService deskService;
    @Autowired
    private DeskMapper deskMapper;

    // Endpoint to retrieve all desks    
    @GetMapping
    public List<DeskDTO> getAllDesks() {
        logger.info("Fetching all desks");
        return deskService.getAllDesks().stream()
                .map(deskMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve a desk by its ID
    @GetMapping("/{id}")
    public ResponseEntity<DeskDTO> getDeskById(@PathVariable Long id) {
        logger.debug("Fetching desk by id: {}", id);
        return deskService.getDeskById(id)
                .map(deskMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a new desk
    @PostMapping
    public ResponseEntity<?> createDesk(@Valid @RequestBody DeskDTO deskDTO) {
        logger.info("Creating new desk: {}", deskDTO);
        if (deskDTO.getDeskName() == null || deskDTO.getDeskName().isBlank()) {
            return ResponseEntity.badRequest().body("Desk name is required");
        }
        var entity = deskMapper.toEntity(deskDTO);
        var saved = deskService.saveDesk(entity);
        return ResponseEntity.ok(deskMapper.toDto(saved));
    }

    // Endpoint to delete a desk by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesk(@PathVariable Long id) {
        logger.warn("Deleting desk with id: {}", id);
        deskService.deleteDesk(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to retrieve all desk names
    @GetMapping("/values")
    public List<String> getAllDeskNames() {
        return deskService.getAllDesks().stream()
                .map(Desk::getDeskName)
                .toList();
    }
}
