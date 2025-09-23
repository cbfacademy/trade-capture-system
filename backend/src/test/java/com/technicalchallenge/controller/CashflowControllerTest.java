package com.technicalchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.technicalchallenge.dto.CashflowDTO;
import com.technicalchallenge.dto.CashflowGenerationRequest;
import com.technicalchallenge.mapper.CashflowMapper;
import com.technicalchallenge.model.Cashflow;
import com.technicalchallenge.model.PayRec;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.service.CashflowService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CashflowController.class)
public class CashflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CashflowService cashflowService;

    @MockBean
    private CashflowMapper cashflowMapper;

    private ObjectMapper objectMapper;
    private CashflowDTO cashflowDTO;
    private Cashflow cashflow;
    private TradeLeg tradeLeg;
    private PayRec payRec;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Set up related entities
        tradeLeg = new TradeLeg();
        tradeLeg.setLegId(1L);
        tradeLeg.setNotional(BigDecimal.valueOf(1000000.0));

        payRec = new PayRec();
        payRec.setId(1L);
        payRec.setPayRec("PAY");

        // Set up CashflowDTO for testing
        cashflowDTO = new CashflowDTO();
        cashflowDTO.setId(1L);
        cashflowDTO.setPaymentValue(BigDecimal.valueOf(25000.0));
        cashflowDTO.setValueDate(LocalDate.now().plusMonths(6));
        cashflowDTO.setRate(0.05);
        cashflowDTO.setPayRec("PAY");

        // Set up Cashflow entity for testing
        cashflow = new Cashflow();
        cashflow.setId(1L);
        cashflow.setTradeLeg(tradeLeg); // Fixed: was setLeg
        cashflow.setPaymentValue(BigDecimal.valueOf(25000.0));
        cashflow.setValueDate(LocalDate.now().plusMonths(6));
        cashflow.setPayRec(payRec);
        cashflow.setRate(0.05);

        // Set up default mappings
        when(cashflowMapper.toDto(any(Cashflow.class))).thenReturn(cashflowDTO);
        when(cashflowMapper.toEntity(any(CashflowDTO.class))).thenReturn(cashflow);
        doNothing().when(cashflowService).populateReferenceDataByName(any(Cashflow.class), any(CashflowDTO.class));
    }

    @Test
    void testGetAllCashflows() throws Exception {
        // Given
        List<Cashflow> cashflows = Arrays.asList(cashflow);
        when(cashflowService.getAllCashflows()).thenReturn(cashflows);

        // When/Then
        mockMvc.perform(get("/api/cashflows")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].paymentValue", is(25000.0)))
                .andExpect(jsonPath("$[0].payRec", is("PAY")));

        verify(cashflowService).getAllCashflows();
    }

    @Test
    void testGetCashflowById() throws Exception {
        // Given
        when(cashflowService.getCashflowById(1L)).thenReturn(Optional.of(cashflow));

        // When/Then
        mockMvc.perform(get("/api/cashflows/1")


                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.paymentValue", is(25000.0)))
                .andExpect(jsonPath("$.payRec", is("PAY")));

        verify(cashflowService).getCashflowById(1L);
    }

    @Test
    void testGetCashflowByIdNotFound() throws Exception {
        // Given
        when(cashflowService.getCashflowById(999L)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/cashflows/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cashflowService).getCashflowById(999L);
    }

    @Test
    void testCreateCashflow() throws Exception {
        // Given
        when(cashflowService.saveCashflow(any(Cashflow.class))).thenReturn(cashflow);

        // When/Then
        mockMvc.perform(post("/api/cashflows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cashflowDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.paymentValue", is(25000.0)));

        verify(cashflowService).saveCashflow(any(Cashflow.class));
        verify(cashflowService).populateReferenceDataByName(any(Cashflow.class), any(CashflowDTO.class));
    }

    @Test
    void testCreateCashflowValidationFailure_NegativePaymentValue() throws Exception {
        // Given
        cashflowDTO.setPaymentValue(BigDecimal.valueOf(-5000.0));

        // When/Then
        mockMvc.perform(post("/api/cashflows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cashflowDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cashflow value must be positive"));

        verify(cashflowService, never()).saveCashflow(any(Cashflow.class));
    }

    @Test
    void testCreateCashflowValidationFailure_MissingValueDate() throws Exception {
        // Given
        cashflowDTO.setValueDate(null);

        // When/Then
        mockMvc.perform(post("/api/cashflows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cashflowDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Value date is required"));

        verify(cashflowService, never()).saveCashflow(any(Cashflow.class));
    }

    @Test
    void testDeleteCashflow() throws Exception {
        // Given
        doNothing().when(cashflowService).deleteCashflow(1L);

        // When/Then
        mockMvc.perform(delete("/api/cashflows/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cashflowService).deleteCashflow(1L);
    }

    @Test
    void testGenerateCashflows() throws Exception {
        // Given
        CashflowGenerationRequest request = new CashflowGenerationRequest();
        request.setTradeStartDate(LocalDate.now());
        request.setTradeMaturityDate(LocalDate.now().plusYears(2));

        CashflowGenerationRequest.TradeLegDTO legDTO = new CashflowGenerationRequest.TradeLegDTO();
        legDTO.setNotional(BigDecimal.valueOf(1000000.0));
        legDTO.setLegType("Fixed");
        legDTO.setRate(0.05);
        legDTO.setCalculationPeriodSchedule("3M");

        request.setLegs(Arrays.asList(legDTO));

        List<CashflowDTO> generatedCashflows = Arrays.asList(cashflowDTO);

        // Mock the controller's behavior for generating cashflows
        // This is a simplification since the actual implementation is in the controller

        // When/Then
        mockMvc.perform(post("/api/cashflows/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGenerateCashflowsWithNoLegs() throws Exception {
        // Given
        CashflowGenerationRequest request = new CashflowGenerationRequest();
        request.setTradeStartDate(LocalDate.now());
        request.setTradeMaturityDate(LocalDate.now().plusYears(2));
        request.setLegs(new ArrayList<>());  // Empty legs list

        // When/Then
        mockMvc.perform(post("/api/cashflows/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
