package com.technicalchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technicalchallenge.dto.SettlementInstructionsUpdateDTO;
import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.mapper.TradeMapper;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.service.SettlementInstructionsService;
import com.technicalchallenge.service.TradeService;
import com.technicalchallenge.service.TradeValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TradeController.class)
class SettlementInstructionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private TradeValidationService validationService;

    @MockBean
    private TradeMapper tradeMapper;

    @MockBean
    private SettlementInstructionsService settlementInstructionsService;

    private static final Long TRADE_ID = 12345L;
    private static final String SETTLEMENT_INSTRUCTIONS = "Wire to Bank XYZ, Account 12345";

    @Test
    void searchBySettlementInstructions_ShouldReturnTrades() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(123L);
        trade.setActive(true);
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setTradeId(123L);

        when(settlementInstructionsService.findTradeIdsBySettlementInstructions("Bank XYZ"))
                .thenReturn(Arrays.asList(123L));
        when(tradeService.getTradeById(123L)).thenReturn(Optional.of(trade));
        when(tradeMapper.toDto(trade)).thenReturn(tradeDTO);

        mockMvc.perform(get("/api/trades/search/settlement-instructions").param("instructions", "Bank XYZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateSettlementInstructions_ShouldReturnOk() throws Exception {
        SettlementInstructionsUpdateDTO request = new SettlementInstructionsUpdateDTO();
        request.setSettlementInstructions(SETTLEMENT_INSTRUCTIONS);
        Trade trade = new Trade();
        trade.setTradeId(TRADE_ID);

        when(tradeService.getTradeById(TRADE_ID)).thenReturn(Optional.of(trade));

        mockMvc.perform(put("/api/trades/{id}/settlement-instructions", TRADE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(settlementInstructionsService).updateSettlementInstructions(TRADE_ID, SETTLEMENT_INSTRUCTIONS);
    }

    @Test
    void updateSettlementInstructions_ShouldReturnNotFound() throws Exception {
        SettlementInstructionsUpdateDTO request = new SettlementInstructionsUpdateDTO();
        request.setSettlementInstructions(SETTLEMENT_INSTRUCTIONS);

        when(tradeService.getTradeById(TRADE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/trades/{id}/settlement-instructions", TRADE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSettlementInstructions_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        SettlementInstructionsUpdateDTO request = new SettlementInstructionsUpdateDTO();
        request.setSettlementInstructions("short");

        mockMvc.perform(put("/api/trades/{id}/settlement-instructions", TRADE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}