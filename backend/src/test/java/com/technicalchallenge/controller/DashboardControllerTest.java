package com.technicalchallenge.controller;

import com.technicalchallenge.dto.DashboardSummaryDTO;
import com.technicalchallenge.dto.TradeBlotterDTO;
import com.technicalchallenge.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @BeforeEach
    public void setup() {
        // Create simple dashboard summary
        DashboardSummaryDTO dashboardSummary = new DashboardSummaryDTO();
        dashboardSummary.setTotalTrades(25L);
        dashboardSummary.setActiveTrades(20L);
        dashboardSummary.setNewTrades(3L);
        dashboardSummary.setTotalNotional(new BigDecimal("150000000.00"));
        dashboardSummary.setLastTradeDate(LocalDate.of(2024, 1, 15));

        // Create simple blotter entry
        TradeBlotterDTO tradeBlotter = new TradeBlotterDTO();
        tradeBlotter.setTradeId(1001L);
        tradeBlotter.setBookName("RATES");
        tradeBlotter.setCounterpartyName("BigBank Corp");
        tradeBlotter.setTradeStatus("LIVE");

        // Mock service responses
        when(dashboardService.getDashboardSummary(any(Long.class))).thenReturn(dashboardSummary);
        
        Page<TradeBlotterDTO> blotterPage = new PageImpl<>(Arrays.asList(tradeBlotter), PageRequest.of(0, 10), 1);
        when(dashboardService.getTradeBlotter(any(Long.class), any(PageRequest.class))).thenReturn(blotterPage);
        
        when(dashboardService.getTraderBlotter(any(Long.class))).thenReturn(Arrays.asList(tradeBlotter));
    }

    @Test
    void shouldReturnDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnTradeBlotter() throws Exception {
        mockMvc.perform(get("/api/dashboard/blotter")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnTraderBlotter() throws Exception {
        mockMvc.perform(get("/api/dashboard/trader/1/blotter"))
                .andExpect(status().isOk());
    }
}