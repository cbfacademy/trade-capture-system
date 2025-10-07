package com.technicalchallenge.controller;

import com.technicalchallenge.dto.CounterpartyDTO;
import com.technicalchallenge.mapper.CounterpartyMapper;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.service.CounterpartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/counterparties")
@Validated
@Tag(name = "Counterparties", description = "Counterparty management for trade settlement and risk management")
public class CounterpartyController { // Purpose of this controller - to manage counterparties, including creating, retrieving, and deleting them.
    @Autowired
    private CounterpartyService counterpartyService;
    @Autowired
    private CounterpartyMapper counterpartyMapper;

    @GetMapping // Endpoint to retrieve all counterparties
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

    @GetMapping("/{id}") // Endpoint to retrieve a counterparty by its ID
    @Operation(summary = "Get counterparty by ID",
               description = "Retrieves detailed information about a specific counterparty")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved counterparty",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = CounterpartyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Counterparty not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CounterpartyDTO> getCounterpartyById(@PathVariable(name = "id") Long id) {
        return counterpartyService.getCounterpartyById(id)
                .map(counterpartyMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // Endpoint to create a new counterparty
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

    @DeleteMapping("/{id}") // Endpoint to delete a counterparty by its ID
    @Operation(summary = "Delete a counterparty",
               description = "Removes a counterparty from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted counterparty"),
        @ApiResponse(responseCode = "404", description = "Counterparty not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCounterparty(@PathVariable(name = "id") Long id) {
        counterpartyService.deleteCounterparty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/values") // Endpoint to retrieve all counterparty names
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
