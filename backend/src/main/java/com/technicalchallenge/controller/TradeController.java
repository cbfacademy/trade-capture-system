package com.technicalchallenge.controller;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.ValidationResult;
import com.technicalchallenge.mapper.TradeMapper;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.service.TradeService;
import com.technicalchallenge.service.TradeValidationService;
import com.technicalchallenge.service.SettlementInstructionsService;
import com.technicalchallenge.dto.SettlementInstructionsUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/trades")
@Validated
@Tag(name = "Trades", description = "Trade management operations including booking, searching, and lifecycle management")
public class TradeController {
    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService tradeService;
    
    @Autowired
    private TradeValidationService validationService;
    @Autowired
    private TradeMapper tradeMapper;
    @Autowired
    private SettlementInstructionsService settlementInstructionsService;

    @GetMapping
    @Operation(summary = "Get all trades",
               description = "Retrieves a list of all trades in the system. Returns comprehensive trade information including legs and cashflows.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all trades",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TradeDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<TradeDTO> getAllTrades() {
        logger.info("Fetching all trades");
        return tradeService.getAllTrades().stream()
                .map(tradeMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get trade by ID",
               description = "Retrieves a specific trade by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trade found and returned successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TradeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Trade not found"),
        @ApiResponse(responseCode = "400", description = "Invalid trade ID format")
    })
    public ResponseEntity<TradeDTO> getTradeById(
            @Parameter(description = "Unique identifier of the trade", required = true)
            @PathVariable(name = "id") Long id) {
        logger.debug("Fetching trade by id: {}", id);
        return tradeService.getTradeById(id)
                .map(tradeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new trade",
               description = "Creates a new trade with the provided details. Automatically generates cashflows and validates business rules.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Trade created successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TradeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid trade data or business rule violation"),
        @ApiResponse(responseCode = "500", description = "Internal server error during trade creation")
    })
    public ResponseEntity<?> createTrade(
            @Parameter(description = "Trade details for creation", required = true)
            @Valid @RequestBody TradeDTO tradeDTO) {
        logger.info("Creating new trade: {}", tradeDTO);
        try {
            Trade trade = tradeMapper.toEntity(tradeDTO);
            tradeService.populateReferenceDataByName(trade, tradeDTO);
            Trade savedTrade = tradeService.saveTrade(trade, tradeDTO);
            TradeDTO responseDTO = tradeMapper.toDto(savedTrade);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            logger.error("Error creating trade: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error creating trade: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing trade",
               description = "Updates an existing trade with new information. Subject to business rule validation and user privileges.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trade updated successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TradeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Trade not found"),
        @ApiResponse(responseCode = "400", description = "Invalid trade data or business rule violation"),
        @ApiResponse(responseCode = "403", description = "Insufficient privileges to update trade")
    })
    public ResponseEntity<?> updateTrade(
            @Parameter(description = "Unique identifier of the trade to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated trade details", required = true)
            @Valid @RequestBody TradeDTO tradeDTO) {
        logger.info("Updating trade with id: {}", id);
        try {
            // tradeDTO.setTradeId(id); // TEMPORARY: Commented out due to Lombok issue
            // tradeDTO.tradeId = id; // Direct field access also not working - field is private
            Trade amendedTrade = tradeService.amendTrade(id, tradeDTO);
            TradeDTO responseDTO = tradeMapper.toDto(amendedTrade);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error updating trade: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error updating trade: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete trade",
               description = "Deletes an existing trade. This is a soft delete that changes the trade status.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trade deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Trade not found"),
        @ApiResponse(responseCode = "400", description = "Trade cannot be deleted in current status"),
        @ApiResponse(responseCode = "403", description = "Insufficient privileges to delete trade")
    })
    public ResponseEntity<?> deleteTrade(
            @Parameter(description = "Unique identifier of the trade to delete", required = true)
            @PathVariable Long id) {
        logger.info("Deleting trade with id: {}", id);
        try {
            tradeService.deleteTrade(id);
            return ResponseEntity.ok().body("Trade cancelled successfully");
        } catch (Exception e) {
            logger.error("Error deleting trade: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error deleting trade: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/terminate")
    @Operation(summary = "Terminate trade",
               description = "Terminates an existing trade before its natural maturity date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trade terminated successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TradeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Trade not found"),
        @ApiResponse(responseCode = "400", description = "Trade cannot be terminated in current status"),
        @ApiResponse(responseCode = "403", description = "Insufficient privileges to terminate trade")
    })
    public ResponseEntity<?> terminateTrade(
            @Parameter(description = "Unique identifier of the trade to terminate", required = true)
            @PathVariable Long id) {
        logger.info("Terminating trade with id: {}", id);
        try {
            Trade terminatedTrade = tradeService.terminateTrade(id);
            TradeDTO responseDTO = tradeMapper.toDto(terminatedTrade);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error terminating trade: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error terminating trade: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel trade",
               description = "Cancels an existing trade by changing its status to cancelled")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trade cancelled successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TradeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Trade not found"),
        @ApiResponse(responseCode = "400", description = "Trade cannot be cancelled in current status"),
        @ApiResponse(responseCode = "403", description = "Insufficient privileges to cancel trade")
    })
    public ResponseEntity<?> cancelTrade(
            @Parameter(description = "Unique identifier of the trade to cancel", required = true)
            @PathVariable Long id) {
        logger.info("Cancelling trade with id: {}", id);
        try {
            Trade cancelledTrade = tradeService.cancelTrade(id);
            TradeDTO responseDTO = tradeMapper.toDto(cancelledTrade);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error cancelling trade: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error cancelling trade: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Multi-criteria search")
    public ResponseEntity<List<TradeDTO>> searchTrades(
            @RequestParam(required = false) String counterpartyName,
            @RequestParam(required = false) String bookName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            List<Trade> trades = tradeService.searchTrades(counterpartyName, bookName, status, startDate, endDate);
            List<TradeDTO> tradeDTOs = trades.stream().map(tradeMapper::toDto).toList();
            return ResponseEntity.ok(tradeDTOs);
        } catch (Exception e) {
            logger.error("Error during trade search: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "Paginated filtering")
    public ResponseEntity<org.springframework.data.domain.Page<TradeDTO>> filterTrades(
            @RequestParam(required = false) String counterpartyName,
            @RequestParam(required = false) String bookName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(page, size);
            org.springframework.data.domain.Page<Trade> tradePage = tradeService.getTradesPaginated(pageable);
            org.springframework.data.domain.Page<TradeDTO> tradeDTOPage = tradePage.map(tradeMapper::toDto);
            return ResponseEntity.ok(tradeDTOPage);
        } catch (Exception e) {
            logger.error("Error during trade filtering: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/rsql")
    @Operation(summary = "RSQL query support for power users")
    public ResponseEntity<?> searchByRsql(@RequestParam String query) {
        try {
            List<Trade> trades = tradeService.searchTradesRsql(query);
            List<TradeDTO> tradeDTOs = trades.stream().map(tradeMapper::toDto).toList();
            return ResponseEntity.ok(tradeDTOs);
        } catch (Exception e) {
            logger.error("Error executing RSQL query: {}", e.getMessage());
            return ResponseEntity.badRequest().body("RSQL query error: " + e.getMessage());
        }
    }

    /**
     * Enhancement 2: Trade Validation Endpoints
     */
    @PostMapping("/validate/create")
    @Operation(summary = "Validate trade for creation",
               description = "Validates trade data against business rules before creation")
    public ResponseEntity<ValidationResult> validateTradeCreation(
            @RequestBody TradeDTO tradeDTO,
            @RequestParam String userLoginId) {
        ValidationResult result = validationService.validateTradeCreation(tradeDTO, userLoginId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate/amend")
    @Operation(summary = "Validate trade for amendment",
               description = "Validates trade amendments against business rules and status requirements")
    public ResponseEntity<ValidationResult> validateTradeAmendment(
            @RequestBody TradeDTO tradeDTO,
            @RequestParam String userLoginId) {
        ValidationResult result = validationService.validateTradeAmendment(tradeDTO, userLoginId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/validate/read")
    @Operation(summary = "Validate user read access",
               description = "Validates if user has appropriate privileges to read trade data")
    public ResponseEntity<ValidationResult> validateTradeRead(@RequestParam String userLoginId) {
        ValidationResult result = validationService.validateTradeRead(userLoginId);
        return ResponseEntity.ok(result);
    }

    /**
     * Step 5: Settlement Instructions Endpoints
     */
    @GetMapping("/search/settlement-instructions")
    @Operation(summary = "Search trades by settlement instructions",
               description = "Find trades containing specific settlement instruction content")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trades found with matching settlement instructions"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<List<TradeDTO>> searchBySettlementInstructions(
            @Parameter(description = "Settlement instructions text to search for", required = true)
            @RequestParam String instructions) {
        logger.info("Searching trades by settlement instructions: {}", instructions);
        try {
            List<Long> tradeIds = settlementInstructionsService.findTradeIdsBySettlementInstructions(instructions);
            List<Trade> trades = tradeIds.stream()
                .map(tradeId -> tradeService.getTradeById(tradeId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Trade::getActive)
                .collect(Collectors.toList());
            List<TradeDTO> tradeDTOs = trades.stream()
                .map(tradeMapper::toDto)
                .toList();
            return ResponseEntity.ok(tradeDTOs);
        } catch (Exception e) {
            logger.error("Error searching by settlement instructions: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/settlement-instructions")
    @Operation(summary = "Update settlement instructions",
               description = "Update settlement instructions for an existing trade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Settlement instructions updated successfully"),
        @ApiResponse(responseCode = "404", description = "Trade not found"),
        @ApiResponse(responseCode = "400", description = "Invalid settlement instructions data")
    })
    public ResponseEntity<?> updateSettlementInstructions(
            @Parameter(description = "Unique identifier of the trade", required = true)
            @PathVariable Long id,
            @Parameter(description = "New settlement instructions", required = true)
            @Valid @RequestBody SettlementInstructionsUpdateDTO request) {
        logger.info("Updating settlement instructions for trade: {}", id);
        try {
            // Verify trade exists
            if (tradeService.getTradeById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            settlementInstructionsService.updateSettlementInstructions(id, request.getSettlementInstructions());
            return ResponseEntity.ok().body("Settlement instructions updated successfully");
        } catch (Exception e) {
            logger.error("Error updating settlement instructions: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error updating settlement instructions: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/settlement-instructions")
    @Operation(summary = "Get settlement instructions",
               description = "Retrieve settlement instructions for an existing trade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Settlement instructions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Trade not found or no settlement instructions exist"),
        @ApiResponse(responseCode = "400", description = "Invalid trade ID")
    })
    public ResponseEntity<?> getSettlementInstructions(
            @Parameter(description = "Unique identifier of the trade", required = true)
            @PathVariable Long id) {
        logger.info("Getting settlement instructions for trade: {}", id);
        try {
            // Verify trade exists
            if (tradeService.getTradeById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String instructions = settlementInstructionsService.getSettlementInstructions(id);
            if (instructions != null) {
                return ResponseEntity.ok().body(new SettlementInstructionsResponse(instructions));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting settlement instructions: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error getting settlement instructions: " + e.getMessage());
        }
    }

    /**
     * Response wrapper for settlement instructions
     */
    public static class SettlementInstructionsResponse {
        private String instructions;

        public SettlementInstructionsResponse(String instructions) {
            this.instructions = instructions;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }
    }
}
