package com.technicalchallenge.controller;

import com.technicalchallenge.dto.CounterpartyDTO;
import com.technicalchallenge.mapper.CounterpartyMapper;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.service.CounterpartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/counterparties")
@Validated
public class CounterpartyController {
    @Autowired
    private CounterpartyService counterpartyService;
    @Autowired
    private CounterpartyMapper counterpartyMapper;

    @GetMapping
    public List<CounterpartyDTO> getAllCounterparties() {
        return counterpartyService.getAllCounterparties().stream()
                .map(counterpartyMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CounterpartyDTO> getCounterpartyById(@PathVariable(name = "id") Long id) {
        return counterpartyService.getCounterpartyById(id)
                .map(counterpartyMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCounterparty(@Valid @RequestBody CounterpartyDTO counterpartyDTO) {
        if (counterpartyDTO.getName() == null || counterpartyDTO.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Counterparty name is required");
        }
        var entity = counterpartyMapper.toEntity(counterpartyDTO);
        var saved = counterpartyService.saveCounterparty(entity);
        return ResponseEntity.ok(counterpartyMapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounterparty(@PathVariable(name = "id") Long id) {
        counterpartyService.deleteCounterparty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/values")
    public List<String> getAllCounterpartyNames() {
        return counterpartyService.getAllCounterparties().stream()
                .map(Counterparty::getName)
                .toList();
    }
}
