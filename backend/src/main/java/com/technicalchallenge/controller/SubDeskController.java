package com.technicalchallenge.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technicalchallenge.dto.SubDeskDTO;
import com.technicalchallenge.mapper.SubDeskMapper;
import com.technicalchallenge.model.SubDesk;
import com.technicalchallenge.service.SubDeskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subdesks")
@Validated
public class SubDeskController {
    private static final Logger logger = LoggerFactory.getLogger(SubDeskController.class);

    @Autowired
    private SubDeskService subDeskService;
    @Autowired
    private SubDeskMapper subDeskMapper;

    @GetMapping
    public List<SubDeskDTO> getAllSubDesks() {
        logger.info("Fetching all subdesks");
        return subDeskService.getAllSubDesks().stream()
                .map(subDeskMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubDeskDTO> getSubDeskById(@PathVariable Long id) {
        logger.debug("Fetching subdesk by id: {}", id);
        return subDeskService.getSubDeskById(id)
                .map(subDeskMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubDesk(@PathVariable Long id) {
        logger.warn("Deleting subdesk with id: {}", id);
        subDeskService.deleteSubDesk(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/values")
    public List<String> getAllSubDeskNames() {
        return subDeskService.getAllSubDesks().stream()
                .map(SubDesk::getSubdeskName)
                .toList();
    }
}
