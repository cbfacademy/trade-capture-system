package com.technicalchallenge.controller;

import com.technicalchallenge.dto.SubDeskDTO;
import com.technicalchallenge.mapper.SubDeskMapper;
import com.technicalchallenge.model.SubDesk;
import com.technicalchallenge.service.SubDeskService;
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
@RequestMapping("/api/subdesks")
@Validated
public class SubDeskController {// Purpose of this controller - to manage subdesks, including creating, retrieving, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(SubDeskController.class);

    // Injecting SubDeskService to handle business logic
    @Autowired
    private SubDeskService subDeskService;

    // Mapper to convert between entity and DTO
    @Autowired
    private SubDeskMapper subDeskMapper;

    // Endpoint to retrieve all subdesks
    @GetMapping
    public List<SubDeskDTO> getAllSubDesks() {
        logger.info("Fetching all subdesks");
        return subDeskService.getAllSubDesks().stream()
                .map(subDeskMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve a subdesk by its ID
    @GetMapping("/{id}")
    public ResponseEntity<SubDeskDTO> getSubDeskById(@PathVariable Long id) {
        logger.debug("Fetching subdesk by id: {}", id);
        return subDeskService.getSubDeskById(id)
                .map(subDeskMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a new subdesk
    @PostMapping
    public ResponseEntity<?> createSubDesk(@Valid @RequestBody SubDeskDTO subDeskDTO) {
        logger.info("Creating new subdesk: {}", subDeskDTO);
        if (subDeskDTO.getSubdeskName() == null || subDeskDTO.getSubdeskName().isBlank()) {
            return ResponseEntity.badRequest().body("Sub desk name is required");
        }
        if (subDeskDTO.getDeskName() == null || subDeskDTO.getDeskName().isBlank()) {
            return ResponseEntity.badRequest().body("Desk name is required");
        }
        var entity = subDeskMapper.toEntity(subDeskDTO);
        var saved = subDeskService.saveSubDesk(entity, subDeskDTO);
        return ResponseEntity.status(201).body(subDeskMapper.toDto(saved));
    }

    // Endpoint to delete a subdesk by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubDesk(@PathVariable Long id) {
        logger.warn("Deleting subdesk with id: {}", id);
        subDeskService.deleteSubDesk(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to retrieve all subdesk names
    @GetMapping("/values")
    public List<String> getAllSubDeskNames() {
        return subDeskService.getAllSubDesks().stream()
                .map(SubDesk::getSubdeskName)
                .toList();
    }
}
