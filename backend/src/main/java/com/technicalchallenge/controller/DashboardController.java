package com.technicalchallenge.controller;

import com.technicalchallenge.dto.DashboardSummaryDTO;
import com.technicalchallenge.dto.TradeBlotterDTO;
import com.technicalchallenge.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(
            @RequestParam(required = false) Long userId) {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/blotter")
    public ResponseEntity<Page<TradeBlotterDTO>> getTradeBlotter(
            @RequestParam(required = false) Long userId,
            Pageable pageable) {
        Page<TradeBlotterDTO> blotter = dashboardService.getTradeBlotter(userId, pageable);
        return ResponseEntity.ok(blotter);
    }

    @GetMapping("/trader/{traderId}/blotter")
    public ResponseEntity<List<TradeBlotterDTO>> getTraderBlotter(
            @PathVariable Long traderId) {
        List<TradeBlotterDTO> blotter = dashboardService.getTraderBlotter(traderId);
        return ResponseEntity.ok(blotter);
    }
}