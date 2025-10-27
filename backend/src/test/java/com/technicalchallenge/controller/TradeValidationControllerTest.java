package com.technicalchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.ValidationResult;
import com.technicalchallenge.mapper.TradeMapper;
import com.technicalchallenge.service.TradeService;
import com.technicalchallenge.service.TradeValidationService;
import com.technicalchallenge.service.SettlementInstructionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TradeController.class)
public class TradeValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private TradeValidationService tradeValidationService;

    @MockBean
    private TradeMapper tradeMapper;

    @MockBean
    private SettlementInstructionsService settlementInstructionsService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Create simple validation result
        ValidationResult validResult = new ValidationResult();
        validResult.setValid(true);
        validResult.setErrors(Arrays.asList());
        validResult.setWarnings(Arrays.asList());

        // Mock service responses
        when(tradeValidationService.validateTradeCreation(any(TradeDTO.class), anyString()))
                .thenReturn(validResult);
        when(tradeValidationService.validateTradeAmendment(any(TradeDTO.class), anyString()))
                .thenReturn(validResult);
        when(tradeValidationService.validateTradeRead(anyString()))
                .thenReturn(validResult);
        when(tradeService.getTradeById(any(Long.class)))
                .thenReturn(Optional.empty()); // Return empty to avoid complex mocking
    }

    @Test
    void shouldReturnValidationResult() throws Exception {
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setTradeId(1001L);
        tradeDTO.setTradeDate(LocalDate.now());

        mockMvc.perform(post("/api/trades/validate/create")
                        .param("userLoginId", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnValidationByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/trades/validate/read")
                        .param("userLoginId", "testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnAmendValidation() throws Exception {
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setTradeId(1001L);
        tradeDTO.setTradeDate(LocalDate.now());

        mockMvc.perform(post("/api/trades/validate/amend")
                        .param("userLoginId", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeDTO)))
                .andExpect(status().isOk());
    }
}