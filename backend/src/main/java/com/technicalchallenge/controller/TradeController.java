package com.technicalchallenge.controller;

import com.technicalchallenge.dto.DailySummaryDTO;
import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeSummaryDTO;
import com.technicalchallenge.mapper.TradeMapper;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.security.Principal; // <-- NEW: Principal import to fix compilation
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TradeController
 *
 * CHANGED / NEW:
 *  - Added multi-criteria search endpoints (/search, /filter, /rsql)
 *  - Added trader/dashboard endpoints (/my-trades, /book/{id}/trades, /summary, /daily-summary)
 *  - Adjusted create/update/delete endpoints to match test expectations (explicit validations,
 *    consistent response codes/messages)
 */
@RestController
@RequestMapping("/api/trades")
@Validated
@Tag(name = "Trades", description = "Trade management operations including booking, searching, and lifecycle management")
public class TradeController {
    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeMapper tradeMapper;

    // ---------------------------------------------------------------------
    // Basic endpoints (unchanged semantics)
    // ---------------------------------------------------------------------

    @GetMapping
    @Operation(summary = "Get all trades",
               description = "Retrieves a list of all trades in the system.")
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
    @Operation(summary = "Get trade by ID", description = "Retrieves a specific trade by its unique identifier")
    public ResponseEntity<TradeDTO> getTradeById(
            @Parameter(description = "Unique identifier of the trade", required = true)
            @PathVariable(name = "id") Long id) {
        logger.debug("Fetching trade by id: {}", id);
        return tradeService.getTradeById(id)
                .map(tradeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------------
    // NEW: Multi-criteria search
    // ---------------------------------------------------------------------
    @GetMapping("/search")
    @Operation(summary = "Search trades by multiple criteria",
               description = "Search trades by counterparty, book, trader, status and date ranges")
    public ResponseEntity<List<TradeDTO>> searchTrades(
            @RequestParam(required = false) String counterparty,
            @RequestParam(required = false) String book,
            @RequestParam(required = false) String trader,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        List<Trade> trades = tradeService.searchTrades(counterparty, book, trader, status, fromDate, toDate);
        return ResponseEntity.ok(trades.stream().map(tradeMapper::toDto).toList());
    }

    // ---------------------------------------------------------------------
    // NEW: Paginated filter
    // ---------------------------------------------------------------------
    @GetMapping("/filter")
    @Operation(summary = "Filter trades with pagination",
               description = "Paginated filtering endpoint for large result sets")
    public ResponseEntity<Page<TradeDTO>> filterTrades(
            @RequestParam(required = false) String counterparty,
            @RequestParam(required = false) String book,
            @RequestParam(required = false) String trader,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "tradeDate,desc") String sort) {

        Page<Trade> pageResult = tradeService.filterTrades(counterparty, book, trader, status, page, size, sort);
        Page<TradeDTO> dtoPage = pageResult.map(tradeMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    // ---------------------------------------------------------------------
    // NEW: RSQL stub endpoint
    // ---------------------------------------------------------------------
    @GetMapping("/rsql")
    @Operation(summary = "RSQL query support (minimal stub)",
               description = "Minimal RSQL support for power users (stub implementation)")
    public ResponseEntity<List<TradeDTO>> rsqlQuery(@RequestParam String query) {
        List<Trade> trades = tradeService.searchByRsql(query);
        return ResponseEntity.ok(trades.stream().map(tradeMapper::toDto).toList());
    }

    // ---------------------------------------------------------------------
    // NEW: Trader and book specific endpoints (dashboard / blotter)
    // ---------------------------------------------------------------------
    @GetMapping("/my-trades")
    public ResponseEntity<List<TradeDTO>> myTrades(Principal principal) {
        String loginId = principal == null ? null : principal.getName();
        if (loginId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Trade> trades = tradeService.findTradesByTrader(loginId);
        return ResponseEntity.ok(trades.stream().map(tradeMapper::toDto).toList());
    }

    @GetMapping("/book/{id}/trades")
    public ResponseEntity<List<TradeDTO>> tradesByBook(@PathVariable Long id) {
        List<Trade> trades = tradeService.findTradesByBook(id);
        return ResponseEntity.ok(trades.stream().map(tradeMapper::toDto).toList());
    }

    // ---------------------------------------------------------------------
    // NEW: Portfolio summary and daily summary endpoints
    // ---------------------------------------------------------------------
    @GetMapping("/summary")
    public ResponseEntity<TradeSummaryDTO> getPortfolioSummary(@RequestParam(required=false) Long bookId) {
        TradeSummaryDTO dto = tradeService.getPortfolioSummary(bookId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/daily-summary")
    public ResponseEntity<DailySummaryDTO> getDailySummary(
            @RequestParam(required = false) String trader,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate d = date == null ? LocalDate.now() : date;
        DailySummaryDTO dto = tradeService.getDailySummary(trader, d);
        return ResponseEntity.ok(dto);
    }

    // ---------------------------------------------------------------------
    // Create / Update / Delete / Terminate / Cancel
    // NOTE: made explicit validation checks to match test expectations
    // ---------------------------------------------------------------------

    // Tests in this project expect 200 OK for create; keep that behavior
    @PostMapping
    public ResponseEntity<?> createTrade(@RequestBody TradeDTO tradeDTO) {
        logger.info("Creating new trade: {}", tradeDTO);

        // ---- explicit validation to match test expectations ----
        if (tradeDTO.getTradeDate() == null) {
            return ResponseEntity.badRequest().body("Trade date is required");
        }
        if ((tradeDTO.getBookName() == null && tradeDTO.getBookId() == null)
                || (tradeDTO.getCounterpartyName() == null && tradeDTO.getCounterpartyId() == null)) {
            return ResponseEntity.badRequest().body("Book and Counterparty are required");
        }
        // --------------------------------------------------------

        try {
            Trade trade = tradeMapper.toEntity(tradeDTO);
            tradeService.populateReferenceDataByName(trade, tradeDTO);
            Trade savedTrade = tradeService.saveTrade(trade, tradeDTO);
            TradeDTO responseDTO = tradeMapper.toDto(savedTrade);
            // return 200 OK to match the existing test suite expectations
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error creating trade: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error creating trade: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing trade",
               description = "Updates an existing trade with new information.")
    public ResponseEntity<?> updateTrade(
            @Parameter(description = "Unique identifier of the trade to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated trade details", required = true)
            @Valid @RequestBody TradeDTO tradeDTO) {

        logger.info("Updating trade with id: {}", id);
        try {
            // Ensure path ID and body ID align
            if (tradeDTO.getTradeId() != null && !tradeDTO.getTradeId().equals(id)) {
                return ResponseEntity.badRequest().body("Trade ID in path must match Trade ID in request body");
            }
            tradeDTO.setTradeId(id);

            Trade trade = tradeMapper.toEntity(tradeDTO);
            tradeService.populateReferenceDataByName(trade, tradeDTO);
            Trade saved = tradeService.saveTrade(trade, tradeDTO);
            TradeDTO responseDTO = tradeMapper.toDto(saved);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error updating trade: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error updating trade: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete trade", description = "Deletes (cancels) an existing trade.")
    public ResponseEntity<?> deleteTrade(
            @Parameter(description = "Unique identifier of the trade to delete", required = true)
            @PathVariable Long id) {

        logger.info("Deleting trade with id: {}", id);
        try {
            tradeService.deleteTrade(id);
            // Tests expect No Content (204) for successful deletion
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting trade: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error deleting trade: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/terminate")
    public ResponseEntity<?> terminateTrade(@PathVariable Long id) {
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
    public ResponseEntity<?> cancelTrade(@PathVariable Long id) {
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
}
