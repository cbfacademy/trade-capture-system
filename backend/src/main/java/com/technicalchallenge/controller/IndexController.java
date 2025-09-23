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
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexMapper indexMapper;

    @GetMapping
    public List<IndexDTO> getAll() {
        logger.info("Fetching all indexes");
        return indexService.findAll().stream()
                .map(indexMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndexDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching index by id: {}", id);
        return indexService.findById(id)
                .map(indexMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IndexDTO> createIndex(@RequestBody IndexDTO indexDTO) {
        logger.info("Creating new index: {}", indexDTO);
        Index saved = indexService.save(indexMapper.toEntity(indexDTO));
        return ResponseEntity.ok(indexMapper.toDto(saved));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting index with id: {}", id);
        if (indexService.findById(id).isPresent()) {
            indexService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/values")
    public List<String> getAllIndexValues() {
        logger.info("Fetching all index values");
        return indexService.findAll().stream()
                .map(Index::getIndex)
                .toList();
    }
}
