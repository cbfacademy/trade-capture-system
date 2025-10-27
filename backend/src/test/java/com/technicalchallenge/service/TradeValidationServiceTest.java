package com.technicalchallenge.service;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.dto.ValidationResult;
import com.technicalchallenge.model.*;
import com.technicalchallenge.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeValidationServiceTest {

    @Mock
    private TradeRepository tradeRepository;
    
    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private CounterpartyRepository counterpartyRepository;

    @InjectMocks
    private TradeValidationService validationService;

    @Test
    void testValidateTradeCreation_ValidTrade() {
        TradeDTO tradeDTO = createValidTradeDTO();
        setupValidRepositoryMocks();
        
        ValidationResult result = validationService.validateTradeCreation(tradeDTO, "testuser");
        
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void testValidateTradeCreation_InvalidDates() {
        TradeDTO tradeDTO = createValidTradeDTO();
        tradeDTO.setTradeStartDate(LocalDate.of(2024, 12, 31));
        tradeDTO.setTradeMaturityDate(LocalDate.of(2024, 1, 1)); // Before start date
        setupValidRepositoryMocks();
        
        ValidationResult result = validationService.validateTradeCreation(tradeDTO, "testuser");
        
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(error -> error.contains("Maturity date cannot be before start date")));
    }

    @Test
    void testValidateTradeCreation_MissingRequiredFields() {
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setTradeId(1L);
        // Missing other required fields
        
        ValidationResult result = validationService.validateTradeCreation(tradeDTO, "testuser");
        
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(error -> error.contains("Book is required")));
    }

    @Test
    void testValidateTradeCreation_InvalidNotional() {
        TradeDTO tradeDTO = createValidTradeDTO();
        TradeLegDTO leg = tradeDTO.getTradeLegs().get(0);
        leg.setNotional(BigDecimal.valueOf(-1000)); // Negative notional
        setupValidRepositoryMocks();
        
        ValidationResult result = validationService.validateTradeCreation(tradeDTO, "testuser");
        
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(error -> error.contains("Notional must be greater than zero")));
    }

    @Test
    void testValidateTradeAmendment_ValidAmendment() {
        TradeDTO tradeDTO = createValidTradeDTO();
        tradeDTO.setId(1L);
        
        Trade existingTrade = new Trade();
        TradeStatus liveStatus = new TradeStatus();
        liveStatus.setId(1000L); // LIVE status allows amendments
        existingTrade.setTradeStatus(liveStatus);
        
        when(tradeRepository.findById(1L)).thenReturn(Optional.of(existingTrade));
        setupValidRepositoryMocks();
        
        ValidationResult result = validationService.validateTradeAmendment(tradeDTO, "testuser");
        
        assertTrue(result.isValid());
    }

    @Test
    void testValidateTradeAmendment_InvalidStatus() {
        TradeDTO tradeDTO = createValidTradeDTO();
        tradeDTO.setId(1L);
        
        Trade existingTrade = new Trade();
        TradeStatus cancelledStatus = new TradeStatus();
        cancelledStatus.setId(2000L); // Status that doesn't allow amendments
        existingTrade.setTradeStatus(cancelledStatus);
        
        when(tradeRepository.findById(1L)).thenReturn(Optional.of(existingTrade));
        
        ValidationResult result = validationService.validateTradeAmendment(tradeDTO, "testuser");
        
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(error -> error.contains("Trade status does not allow amendments")));
    }

    @Test
    void testValidateTradeRead_AlwaysValid() {
        ValidationResult result = validationService.validateTradeRead("testuser");
        
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    private TradeDTO createValidTradeDTO() {
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setTradeId(12345L);
        tradeDTO.setTradeDate(LocalDate.of(2024, 1, 15));
        tradeDTO.setTradeStartDate(LocalDate.of(2024, 1, 15));
        tradeDTO.setTradeMaturityDate(LocalDate.of(2025, 1, 15));
        tradeDTO.setBookId(1L);
        tradeDTO.setCounterpartyId(1L);
        tradeDTO.setTradeTypeId(1L);

        // Create valid trade legs
        TradeLegDTO leg1 = new TradeLegDTO();
        leg1.setNotional(BigDecimal.valueOf(1000000));
        leg1.setRate(0.05);
        leg1.setCurrencyId(1L);
        leg1.setPayRecId(1000L); // Pay

        TradeLegDTO leg2 = new TradeLegDTO();
        leg2.setNotional(BigDecimal.valueOf(1000000));
        leg2.setRate(0.03);
        leg2.setCurrencyId(1L);
        leg2.setPayRecId(1001L); // Receive

        tradeDTO.setTradeLegs(Arrays.asList(leg1, leg2));

        return tradeDTO;
    }

    private void setupValidRepositoryMocks() {
        // Mock valid book
        Book book = new Book();
        book.setId(1L);
        book.setBookName("RATES");
        book.setActive(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Mock valid counterparty
        Counterparty counterparty = new Counterparty();
        counterparty.setId(1L);
        counterparty.setName("Test Bank");
        counterparty.setActive(true);
        when(counterpartyRepository.findById(1L)).thenReturn(Optional.of(counterparty));

        // Mock no existing trades with same ID
        when(tradeRepository.findByTradeId(anyLong())).thenReturn(Arrays.asList());
    }
}