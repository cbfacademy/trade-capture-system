package com.technicalchallenge.service;

import com.technicalchallenge.dto.DashboardSummaryDTO;
import com.technicalchallenge.dto.TradeBlotterDTO;
import com.technicalchallenge.model.*;
import com.technicalchallenge.repository.TradeRepository;
import com.technicalchallenge.repository.CashflowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private CashflowRepository cashflowRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void testGetDashboardSummary() {
        List<Trade> mockTrades = createMockTrades();
        when(tradeRepository.findAll()).thenReturn(mockTrades);

        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(null);

        assertNotNull(summary);
        assertEquals(3, summary.getTotalTrades());
        assertEquals(3, summary.getActiveTrades());
        // Total notional = 2M + 1.5M + 1.5M = 5M, but each trade has 2 legs so 10M total
        assertTrue(summary.getTotalNotional().compareTo(BigDecimal.valueOf(10000000)) == 0);
        verify(tradeRepository).findAll();
    }

    @Test
    void testGetTradeBlotter() {
        List<Trade> mockTrades = createMockTrades();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Trade> tradePage = new PageImpl<>(mockTrades, pageable, mockTrades.size());
        when(tradeRepository.findAllActivePaginated(pageable)).thenReturn(tradePage);
        when(cashflowRepository.countByTradeLegTradeTradeId(anyLong())).thenReturn(2L);

        Page<TradeBlotterDTO> blotter = dashboardService.getTradeBlotter(null, pageable);

        assertNotNull(blotter);
        assertEquals(3, blotter.getContent().size());
        
        TradeBlotterDTO firstTrade = blotter.getContent().get(0);
        assertNotNull(firstTrade.getTradeId());
        assertNotNull(firstTrade.getCounterpartyName());
        assertNotNull(firstTrade.getBookName());
        assertNotNull(firstTrade.getTradeStatus());
        
        verify(tradeRepository).findAllActivePaginated(pageable);
    }

    @Test
    void testGetTraderBlotter() {
        Long traderId = 1L;
        List<Trade> mockTrades = createMockTradesForTrader();
        when(tradeRepository.findByTraderUserId(traderId)).thenReturn(mockTrades);
        when(cashflowRepository.countByTradeLegTradeTradeId(anyLong())).thenReturn(2L);

        List<TradeBlotterDTO> blotter = dashboardService.getTraderBlotter(traderId);

        assertNotNull(blotter);
        assertEquals(2, blotter.size());
        
        // Verify all trades belong to the trader
        for (TradeBlotterDTO trade : blotter) {
            assertNotNull(trade.getTraderUserName());
        }
        
        verify(tradeRepository).findByTraderUserId(traderId);
    }

    @Test
    void testGetDashboardSummary_EmptyTrades() {
        when(tradeRepository.findAll()).thenReturn(Arrays.asList());

        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(null);

        assertNotNull(summary);
        assertEquals(0, summary.getTotalTrades());
        assertEquals(0, summary.getActiveTrades());
        assertTrue(summary.getTotalNotional().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    void testGetTradeBlotter_EmptyTrades() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Trade> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);
        when(tradeRepository.findAllActivePaginated(pageable)).thenReturn(emptyPage);

        Page<TradeBlotterDTO> blotter = dashboardService.getTradeBlotter(null, pageable);

        assertNotNull(blotter);
        assertTrue(blotter.getContent().isEmpty());
    }

    private List<Trade> createMockTrades() {
        Trade trade1 = createTrade(1001L, "LIVE", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(2000000));
        Trade trade2 = createTrade(1002L, "LIVE", LocalDate.of(2025, 6, 30), BigDecimal.valueOf(1500000));
        Trade trade3 = createTrade(1003L, "MATURED", LocalDate.of(2023, 12, 31), BigDecimal.valueOf(1500000));
        
        return Arrays.asList(trade1, trade2, trade3);
    }

    private List<Trade> createMockTradesForTrader() {
        Trade trade1 = createTrade(1001L, "LIVE", LocalDate.of(2025, 12, 31), BigDecimal.valueOf(2000000));
        Trade trade2 = createTrade(1002L, "LIVE", LocalDate.of(2025, 6, 30), BigDecimal.valueOf(1500000));
        
        // Set trader for both trades
        ApplicationUser trader = new ApplicationUser();
        trader.setId(1L);
        trader.setFirstName("John");
        trader.setLastName("Trader");
        trade1.setTraderUser(trader);
        trade2.setTraderUser(trader);
        
        return Arrays.asList(trade1, trade2);
    }

    private Trade createTrade(Long tradeId, String status, LocalDate maturityDate, BigDecimal notional) {
        Trade trade = new Trade();
        trade.setTradeId(tradeId);
        trade.setTradeDate(LocalDate.of(2024, 1, 15));
        trade.setTradeStartDate(LocalDate.of(2024, 1, 15));
        trade.setTradeMaturityDate(maturityDate);
        trade.setActive(true);
        trade.setCreatedDate(LocalDateTime.now());

        // Set counterparty
        Counterparty counterparty = new Counterparty();
        counterparty.setName("Test Bank " + tradeId);
        trade.setCounterparty(counterparty);

        // Set book
        Book book = new Book();
        book.setBookName("RATES");
        trade.setBook(book);

        // Set trade status
        TradeStatus tradeStatus = new TradeStatus();
        tradeStatus.setTradeStatus(status);
        trade.setTradeStatus(tradeStatus);

        // Set trade legs with notional
        TradeLeg leg1 = new TradeLeg();
        leg1.setNotional(notional);
        leg1.setRate(0.05);

        TradeLeg leg2 = new TradeLeg();
        leg2.setNotional(notional);
        leg2.setRate(0.03);

        trade.setTradeLegs(Arrays.asList(leg1, leg2));

        return trade;
    }
}