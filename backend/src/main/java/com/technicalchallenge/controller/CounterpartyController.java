package com.technicalchallenge.controller;

import java.util.List;

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

import com.technicalchallenge.dto.CounterpartyDTO;
import com.technicalchallenge.mapper.CounterpartyMapper;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.service.CounterpartyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/counterparties")
@Validated
@Tag(name = "Counterparties", description = "Counterparty management for trade settlement and risk management")
public class CounterpartyController {
    @Autowired
    private CounterpartyService counterpartyService;
    @Autowired
    private CounterpartyMapper counterpartyMapper;

    @GetMapping
    @Operation(summary = "Get all counterparties",
               description = "Retrieves a list of all counterparties available for trading")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all counterparties",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = CounterpartyDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<CounterpartyDTO> getAllCounterparties() {
        return counterpartyService.getAllCounterparties().stream()
                .map(counterpartyMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get counterparty by ID",
               description = "Retrieves detailed information about a specific counterparty")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved counterparty",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = CounterpartyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Counterparty not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CounterpartyDTO> getCounterpartyById(@PathVariable Long id) {
        return counterpartyService.getCounterpartyById(id)
                .map(counterpartyMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new counterparty",
               description = "Adds a new counterparty to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created counterparty",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = CounterpartyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createCounterparty(@Valid @RequestBody CounterpartyDTO counterpartyDTO) {
        if (counterpartyDTO.getName() == null || counterpartyDTO.getName().isBlank()) {
            return ResponseEntity.badRequest().body("Counterparty name is required");
        }
        var entity = counterpartyMapper.toEntity(counterpartyDTO);
        var saved = counterpartyService.saveCounterparty(entity);
        return ResponseEntity.ok(counterpartyMapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a counterparty",
               description = "Removes a counterparty from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted counterparty"),
        @ApiResponse(responseCode = "404", description = "Counterparty not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCounterparty(@PathVariable Long id) {
        counterpartyService.deleteCounterparty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/values")
    @Operation(summary = "Get all counterparty names",
               description = "Retrieves a list of all counterparty names")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all counterparty names",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<String> getAllCounterpartyNames() {
        return counterpartyService.getAllCounterparties().stream()
                .map(Counterparty::getName)
                .toList();
    }
}
