package com.technicalchallenge.tradeleg;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.technicalchallenge.businessdayconvention.BusinessDayConventionRepository;
import com.technicalchallenge.currency.Currency;
import com.technicalchallenge.currency.CurrencyRepository;
import com.technicalchallenge.holidaycalendar.HolidayCalendarRepository;
import com.technicalchallenge.index.IndexRepository;
import com.technicalchallenge.legtype.LegType;
import com.technicalchallenge.legtype.LegTypeRepository;
import com.technicalchallenge.payrec.PayRecRepository;
import com.technicalchallenge.schedule.ScheduleRepository;
import com.technicalchallenge.trade.Trade;

@ExtendWith(MockitoExtension.class)
public class TradeLegServiceTest {

    @Mock
    private TradeLegRepository tradeLegRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private LegTypeRepository legTypeRepository;

    @Mock
    private IndexRepository indexRepository;

    @Mock
    private HolidayCalendarRepository holidayCalendarRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private BusinessDayConventionRepository businessDayConventionRepository;

    @Mock
    private PayRecRepository payRecRepository;

    @InjectMocks
    private TradeLegService tradeLegService;

    private TradeLeg tradeLeg1;
    private TradeLeg tradeLeg2;
    private List<TradeLeg> tradeLegList;
    private Trade trade;
    private Currency currency;
    private LegType legType;

    @BeforeEach
    void setUp() {
        // Set up related entities
        trade = new Trade();
        trade.setId(1L);
        trade.setTradeId(1001L);

        currency = new Currency();
        currency.setId(1L);
        currency.setCurrency("USD");

        legType = new LegType();
        legType.setId(1L);
        legType.setType("Fixed");

        // Set up first TradeLeg
        tradeLeg1 = new TradeLeg();
        tradeLeg1.setLegId(1L);
        tradeLeg1.setTrade(trade);
        tradeLeg1.setNotional(BigDecimal.valueOf(1000000.0));
        tradeLeg1.setRate(0.05);
        tradeLeg1.setCurrency(currency);
        tradeLeg1.setLegRateType(legType);

        // Set up second TradeLeg
        tradeLeg2 = new TradeLeg();
        tradeLeg2.setLegId(2L);
        tradeLeg2.setTrade(trade);
        tradeLeg2.setNotional(BigDecimal.valueOf(1000000.0));
        tradeLeg2.setRate(0.03);
        tradeLeg2.setCurrency(currency);
        tradeLeg2.setLegRateType(legType);

        // Set up trade legs list
        tradeLegList = Arrays.asList(tradeLeg1, tradeLeg2);
    }

    @Test
    void testGetAllTradeLegs() {
        // Given
        when(tradeLegRepository.findAll()).thenReturn(tradeLegList);

        // When
        List<TradeLeg> result = tradeLegService.getAllTradeLegs();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(tradeLeg1.getLegId(), result.get(0).getLegId());
        assertEquals(tradeLeg2.getLegId(), result.get(1).getLegId());
        verify(tradeLegRepository).findAll();
    }

    @Test
    void testGetTradeLegById() {
        // Given
        when(tradeLegRepository.findById(1L)).thenReturn(Optional.of(tradeLeg1));

        // When
        Optional<TradeLeg> result = tradeLegService.getTradeLegById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getLegId());
        assertEquals(BigDecimal.valueOf(1000000.0), result.get().getNotional());
        assertEquals(0.05, result.get().getRate());
        assertEquals(currency, result.get().getCurrency());
        assertEquals(legType, result.get().getLegRateType());
        verify(tradeLegRepository).findById(1L);
    }

    @Test
    void testGetTradeLegByNonExistentId() {
        // Given
        when(tradeLegRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<TradeLeg> result = tradeLegService.getTradeLegById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(tradeLegRepository).findById(999L);
    }

    @Test
    void testSaveTradeLeg() {
        // Given
        TradeLeg newTradeLeg = new TradeLeg();
        newTradeLeg.setTrade(trade);
        newTradeLeg.setNotional(BigDecimal.valueOf(2000000.0));
        newTradeLeg.setRate(0.04);
        newTradeLeg.setCurrency(currency);
        newTradeLeg.setLegRateType(legType);

        when(tradeLegRepository.save(any(TradeLeg.class))).thenReturn(newTradeLeg);

        // When
        TradeLeg savedTradeLeg = tradeLegService.saveTradeLeg(newTradeLeg, null);

        // Then
        assertNotNull(savedTradeLeg);
        assertEquals(BigDecimal.valueOf(2000000.0), savedTradeLeg.getNotional());
        assertEquals(0.04, savedTradeLeg.getRate());
        verify(tradeLegRepository).save(newTradeLeg);
    }

    @Test
    void testDeleteTradeLeg() {
        // Given
        Long tradeLegId = 1L;
        doNothing().when(tradeLegRepository).deleteById(tradeLegId);

        // When
        tradeLegService.deleteTradeLeg(tradeLegId);

        // Then
        verify(tradeLegRepository).deleteById(tradeLegId);
    }
}
