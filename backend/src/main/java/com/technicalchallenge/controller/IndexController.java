package com.technicalchallenge.controller;

import com.technicalchallenge.dto.IndexDTO;
import com.technicalchallenge.mapper.IndexMapper;
import com.technicalchallenge.model.Index;
import com.technicalchallenge.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/indices")
public class IndexController {// Purpose of this controller - to manage indices, including creating, retrieving, updating, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    // Injecting IndexService to handle business logic
    @Autowired
    private IndexService indexService;

    // Mapper to convert between entity and DTO
    @Autowired
    private IndexMapper indexMapper;

    // Endpoint to retrieve all indices
    @GetMapping
    public List<IndexDTO> getAll() {
        logger.info("Fetching all indexes");
        return indexService.findAll().stream()
                .map(indexMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve an index by its ID
    @GetMapping("/{id}")
    public ResponseEntity<IndexDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching index by id: {}", id);
        return indexService.findById(id)
                .map(indexMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Endpoint to create a new index
    @PostMapping
    public ResponseEntity<IndexDTO> createIndex(@RequestBody IndexDTO indexDTO) {
        logger.info("Creating new index: {}", indexDTO);
        Index saved = indexService.save(indexMapper.toEntity(indexDTO));
        return ResponseEntity.ok(indexMapper.toDto(saved));
    }

    // Endpoint to update an existing index
    @PutMapping("/{id}")
    public ResponseEntity<IndexDTO> update(@PathVariable Long id, @RequestBody IndexDTO indexDTO) {
        return indexService.findById(id)
                .map(existing -> {
                    Index entity = indexMapper.toEntity(indexDTO);
                    entity.setId(id);
                    return ResponseEntity.ok(indexMapper.toDto(indexService.save(entity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to delete an index by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting index with id: {}", id);
        if (indexService.findById(id).isPresent()) {
            indexService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to retrieve all index values
    @GetMapping("/values")
    public List<String> getAllIndexValues() {
        logger.info("Fetching all index values");
        return indexService.findAll().stream()
                .map(Index::getIndex)
                .toList();
    }
}
