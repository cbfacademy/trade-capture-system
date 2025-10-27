package com.technicalchallenge.controller;

import com.technicalchallenge.mapper.TradeMapper;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.service.TradeService;
import com.technicalchallenge.service.TradeValidationService;
import com.technicalchallenge.service.SettlementInstructionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TradeController.class)
public class TradeSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private TradeMapper tradeMapper;

    @MockBean
    private TradeValidationService tradeValidationService;

    @MockBean
    private SettlementInstructionsService settlementInstructionsService;

    @BeforeEach
    public void setup() {
        // Create simple test data
        Trade trade = new Trade();
        trade.setId(1L);
        trade.setTradeId(1001L);
        trade.setTradeDate(LocalDate.of(2024, 1, 15));

        // Mock service responses to return empty lists to avoid complex mocking
        when(tradeService.searchTradesByStatus(anyString())).thenReturn(Arrays.asList());
        when(tradeService.searchTradesByCounterparty(anyString())).thenReturn(Arrays.asList());
        when(tradeService.searchTradesByBook(anyString())).thenReturn(Arrays.asList());
        when(tradeService.searchTrades(anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Arrays.asList());
        when(tradeService.searchTradesByDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(Arrays.asList());
        when(tradeService.searchTradesRsql(anyString())).thenReturn(Arrays.asList());
        
        // Mock pagination - this is what was missing for the filter endpoint
        org.springframework.data.domain.Page<Trade> emptyPage =
            new org.springframework.data.domain.PageImpl<>(Arrays.asList(),
                org.springframework.data.domain.PageRequest.of(0, 10), 0);
        when(tradeService.getTradesPaginated(any(org.springframework.data.domain.Pageable.class))).thenReturn(emptyPage);
    }

    @Test
    void shouldReturnSearchByStatus() throws Exception {
        mockMvc.perform(get("/api/trades/search")
                        .param("status", "LIVE"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnSearchByCounterparty() throws Exception {
        mockMvc.perform(get("/api/trades/search")
                        .param("counterparty", "BigBank"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnSearchByBook() throws Exception {
        mockMvc.perform(get("/api/trades/search")
                        .param("book", "RATES"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnFilterTrades() throws Exception {
        mockMvc.perform(get("/api/trades/filter")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnRsqlSearch() throws Exception {
        mockMvc.perform(get("/api/trades/rsql")
                        .param("query", "counterparty.name==BigBank"))
                .andExpect(status().isOk());
    }
}