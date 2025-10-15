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

import com.technicalchallenge.dto.PayRecDTO;
import com.technicalchallenge.mapper.PayRecMapper;
import com.technicalchallenge.model.PayRec;
import com.technicalchallenge.service.PayRecService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/payRecs")
@Tag(name = "Pay/Receive Flags", description = "Payment direction reference data for trade legs")
public class PayRecController {
    private static final Logger logger = LoggerFactory.getLogger(PayRecController.class);

    @Autowired
    private PayRecService payRecService;

    @Autowired
    private PayRecMapper payRecMapper;

    @GetMapping
    public List<PayRecDTO> getAll() {
        logger.info("Fetching all pay recs");
        return payRecService.findAll().stream()
                .map(payRecMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayRecDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching pay rec by id: {}", id);
        return payRecService.findById(id)
                .map(payRecMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PayRecDTO create(@RequestBody PayRecDTO payRecDTO) {
        logger.info("Creating new pay rec: {}", payRecDTO);
        PayRec entity = payRecMapper.toEntity(payRecDTO);
        return payRecMapper.toDto(payRecService.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayRecDTO> update(@PathVariable Long id, @RequestBody PayRecDTO payRecDTO) {
        return payRecService.findById(id)
                .map(existing -> {
                    PayRec entity = payRecMapper.toEntity(payRecDTO);
                    entity.setId(id);
                    return ResponseEntity.ok(payRecMapper.toDto(payRecService.save(entity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting pay rec with id: {}", id);
        if (payRecService.findById(id).isPresent()) {
            payRecService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/values")
    public List<String> getAllPayRecValues() {
        logger.info("Fetching all pay/receive values");
        return payRecService.findAll().stream()
                .map(PayRec::getPayRec)
                .toList();
    }
}
