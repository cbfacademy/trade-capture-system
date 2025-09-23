package com.technicalchallenge.service;

import com.technicalchallenge.model.Cashflow;
import com.technicalchallenge.model.PayRec;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.repository.CashflowRepository;
import com.technicalchallenge.repository.BusinessDayConventionRepository;
import com.technicalchallenge.repository.LegTypeRepository;
import com.technicalchallenge.repository.PayRecRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CashflowServiceTest {

    @Mock
    private CashflowRepository cashflowRepository;

    @Mock
    private PayRecRepository payRecRepository;

    @Mock
    private LegTypeRepository legTypeRepository;

    @Mock
    private BusinessDayConventionRepository businessDayConventionRepository;

    @InjectMocks
    private CashflowService cashflowService;

    private Cashflow cashflow1;
    private Cashflow cashflow2;
    private List<Cashflow> cashflowList;
    private TradeLeg tradeLeg;
    private PayRec payRec;

    @BeforeEach
    void setUp() {
        // Set up related entities
        tradeLeg = new TradeLeg();
        tradeLeg.setLegId(1L);
        tradeLeg.setNotional(BigDecimal.valueOf(1000000.0));

        payRec = new PayRec();
        payRec.setId(1L);
        payRec.setPayRec("PAY");

        // Set up first Cashflow
        cashflow1 = new Cashflow();
        cashflow1.setId(1L);
        cashflow1.setTradeLeg(tradeLeg); // Fixed: was setLeg
        cashflow1.setPaymentValue(BigDecimal.valueOf(25000.0));
        cashflow1.setValueDate(LocalDate.now().plusMonths(6));
        cashflow1.setPayRec(payRec);
        cashflow1.setRate(0.05);
        cashflow1.setValidityStartDate(LocalDate.now().minusDays(1)); // Fixed: LocalDate instead of LocalDateTime
        cashflow1.setValidityEndDate(null);

        // Set up second Cashflow
        cashflow2 = new Cashflow();
        cashflow2.setId(2L);
        cashflow2.setTradeLeg(tradeLeg); // Fixed: was setLeg
        cashflow2.setPaymentValue(BigDecimal.valueOf(25000.0));
        cashflow2.setValueDate(LocalDate.now().plusYears(1));
        cashflow2.setPayRec(payRec);
        cashflow2.setRate(0.05);
        cashflow2.setValidityStartDate(LocalDate.now().minusDays(1)); // Fixed: LocalDate instead of LocalDateTime
        cashflow2.setValidityEndDate(null);

        // Set up cashflow list
        cashflowList = Arrays.asList(cashflow1, cashflow2);
    }

    @Test
    void testGetAllCashflows() {
        // Given
        when(cashflowRepository.findAll()).thenReturn(cashflowList);

        // When
        List<Cashflow> result = cashflowService.getAllCashflows();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(cashflow1.getId(), result.get(0).getId());
        assertEquals(cashflow2.getId(), result.get(1).getId());
        verify(cashflowRepository).findAll();
    }

    @Test
    void testGetCashflowById() {
        // Given
        when(cashflowRepository.findById(1L)).thenReturn(Optional.of(cashflow1));

        // When
        Optional<Cashflow> result = cashflowService.getCashflowById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(BigDecimal.valueOf(25000.0), result.get().getPaymentValue());
        assertEquals(payRec, result.get().getPayRec());
        verify(cashflowRepository).findById(1L);
    }

    @Test
    void testGetCashflowByNonExistentId() {
        // Given
        when(cashflowRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Cashflow> result = cashflowService.getCashflowById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(cashflowRepository).findById(999L);
    }

    @Test
    void testSaveCashflow() {
        // Given
        Cashflow newCashflow = new Cashflow();
        newCashflow.setTradeLeg(tradeLeg); // Fixed: was setLeg
        newCashflow.setPaymentValue(BigDecimal.valueOf(30000.0));
        newCashflow.setValueDate(LocalDate.now().plusMonths(9));
        newCashflow.setPayRec(payRec);
        newCashflow.setRate(0.04);

        when(cashflowRepository.save(any(Cashflow.class))).thenReturn(newCashflow);

        // When
        Cashflow savedCashflow = cashflowService.saveCashflow(newCashflow);

        // Then
        assertNotNull(savedCashflow);
        assertEquals(BigDecimal.valueOf(30000.0), savedCashflow.getPaymentValue());
        assertEquals(0.04, savedCashflow.getRate());
        verify(cashflowRepository).save(newCashflow);
    }

    @Test
    void testSaveCashflowWithInvalidPaymentValue() {
        // Given
        Cashflow invalidCashflow = new Cashflow();
        invalidCashflow.setTradeLeg(tradeLeg); // Fixed: was setLeg
        invalidCashflow.setPaymentValue(BigDecimal.valueOf(-10000.0)); // Negative value
        invalidCashflow.setValueDate(LocalDate.now().plusMonths(3));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> cashflowService.saveCashflow(invalidCashflow)); // Fixed: expression lambda
        verify(cashflowRepository, never()).save(any(Cashflow.class));
    }

    @Test
    void testSaveCashflowWithMissingValueDate() {
        // Given
        Cashflow invalidCashflow = new Cashflow();
        invalidCashflow.setTradeLeg(tradeLeg); // Fixed: was setLeg
        invalidCashflow.setPaymentValue(BigDecimal.valueOf(15000.0));
        invalidCashflow.setValueDate(null); // Missing value date

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> cashflowService.saveCashflow(invalidCashflow)); // Fixed: expression lambda
        verify(cashflowRepository, never()).save(any(Cashflow.class));
    }

    @Test
    void testDeleteCashflow() {
        // Given
        Long cashflowId = 1L;
        doNothing().when(cashflowRepository).deleteById(cashflowId);

        // When
        cashflowService.deleteCashflow(cashflowId);

        // Then
        verify(cashflowRepository).deleteById(cashflowId);
    }
}
