package com.technicalchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.mapper.TradeLegMapper;
import com.technicalchallenge.model.Currency;
import com.technicalchallenge.model.LegType;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.service.TradeLegService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TradeLegController.class)
public class TradeLegControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeLegService tradeLegService;

    @MockBean
    private TradeLegMapper tradeLegMapper;

    private ObjectMapper objectMapper;
    private TradeLegDTO tradeLegDTO;
    private TradeLeg tradeLeg;
    private Currency currency;
    private LegType legType;
    private Trade trade;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Set up related entities
        currency = new Currency();
        currency.setId(1L);
        currency.setCurrency("USD");

        legType = new LegType();
        legType.setId(1L);
        legType.setType("Fixed");

        trade = new Trade();
        trade.setId(1L);
        trade.setTradeId(1001L);

        // Set up TradeLegDTO for testing
        tradeLegDTO = new TradeLegDTO();
        tradeLegDTO.setLegId(1L);
        tradeLegDTO.setNotional(BigDecimal.valueOf(1000000.0));
        tradeLegDTO.setRate(0.05);
        tradeLegDTO.setCurrency("USD");
        tradeLegDTO.setLegType("Fixed");

        // Set up TradeLeg entity for testing
        tradeLeg = new TradeLeg();
        tradeLeg.setLegId(1L);
        tradeLeg.setTrade(trade);
        tradeLeg.setNotional(BigDecimal.valueOf(1000000.0));
        tradeLeg.setRate(0.05);
        tradeLeg.setCurrency(currency);
        tradeLeg.setLegRateType(legType);

        // Set up default mappings
        when(tradeLegMapper.toDto(any(TradeLeg.class))).thenReturn(tradeLegDTO);
        when(tradeLegMapper.toEntity(any(TradeLegDTO.class))).thenReturn(tradeLeg);
    }

    @Test
    void testGetAllTradeLegs() throws Exception {
        // Given
        List<TradeLeg> tradeLegs = Arrays.asList(tradeLeg);
        when(tradeLegService.getAllTradeLegs()).thenReturn(tradeLegs);

        // When/Then
        mockMvc.perform(get("/api/tradeLegs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].legId", is(1)))
                .andExpect(jsonPath("$[0].notional", is(1000000.0)))
                .andExpect(jsonPath("$[0].currency", is("USD")));

        verify(tradeLegService).getAllTradeLegs();
    }

    @Test
    void testGetTradeLegById() throws Exception {
        // Given
        when(tradeLegService.getTradeLegById(1L)).thenReturn(Optional.of(tradeLeg));

        // When/Then
        mockMvc.perform(get("/api/tradeLegs/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.legId", is(1)))
                .andExpect(jsonPath("$.notional", is(1000000.0)))
                .andExpect(jsonPath("$.currency", is("USD")));

        verify(tradeLegService).getTradeLegById(1L);
    }

    @Test
    void testGetTradeLegByIdNotFound() throws Exception {
        // Given
        when(tradeLegService.getTradeLegById(999L)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/tradeLegs/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(tradeLegService).getTradeLegById(999L);
    }

    @Test
    void testCreateTradeLeg() throws Exception {
        // Given
        when(tradeLegService.saveTradeLeg(any(TradeLeg.class), any(TradeLegDTO.class))).thenReturn(tradeLeg);

        // When/Then
        mockMvc.perform(post("/api/tradeLegs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tradeLegDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.legId", is(1)))
                .andExpect(jsonPath("$.notional", is(1000000.0)));

        verify(tradeLegService).saveTradeLeg(any(TradeLeg.class), any(TradeLegDTO.class));
    }

    @Test
    void testCreateTradeLegValidationFailure_NegativeNotional() throws Exception {
        // Given
        tradeLegDTO.setNotional(BigDecimal.valueOf(-1000000.0));

        // When/Then
        mockMvc.perform(post("/api/tradeLegs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tradeLegDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Notional must be positive"));

        verify(tradeLegService, never()).saveTradeLeg(any(TradeLeg.class), any(TradeLegDTO.class));
    }

    @Test
    void testCreateTradeLegValidationFailure_MissingCurrency() throws Exception {
        // Given
        tradeLegDTO.setCurrency(null);

        // When/Then
        mockMvc.perform(post("/api/tradeLegs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tradeLegDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Currency and Leg Rate Type are required"));

        verify(tradeLegService, never()).saveTradeLeg(any(TradeLeg.class), any(TradeLegDTO.class));
    }

    @Test
    void testCreateTradeLegValidationFailure_MissingLegType() throws Exception {
        // Given
        tradeLegDTO.setLegType(null);

        // When/Then
        mockMvc.perform(post("/api/tradeLegs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tradeLegDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Currency and Leg Rate Type are required"));

        verify(tradeLegService, never()).saveTradeLeg(any(TradeLeg.class), any(TradeLegDTO.class));
    }

    @Test
    void testDeleteTradeLeg() throws Exception {
        // Given
        doNothing().when(tradeLegService).deleteTradeLeg(1L);

        // When/Then
        mockMvc.perform(delete("/api/tradeLegs/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(tradeLegService).deleteTradeLeg(1L);
    }
}
