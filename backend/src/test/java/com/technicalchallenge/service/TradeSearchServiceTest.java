package com.technicalchallenge.service;

import com.technicalchallenge.model.Trade;
import com.technicalchallenge.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeSearchServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @Test
    void testSearchByCounterparty() {
        List<Trade> trades = Arrays.asList(new Trade());
        when(tradeRepository.findByCounterpartyNameContaining("BigBank")).thenReturn(trades);

        List<Trade> result = tradeService.searchTradesByCounterparty("BigBank");
        assertEquals(1, result.size());
        verify(tradeRepository).findByCounterpartyNameContaining("BigBank");
    }

    @Test
    void testSearchByBook() {
        List<Trade> trades = Arrays.asList(new Trade());
        when(tradeRepository.findByBookNameContaining("RATES")).thenReturn(trades);

        List<Trade> result = tradeService.searchTradesByBook("RATES");
        assertEquals(1, result.size());
        verify(tradeRepository).findByBookNameContaining("RATES");
    }

    @Test
    void testSearchByStatus() {
        List<Trade> trades = Arrays.asList(new Trade());
        when(tradeRepository.findByTradeStatus("LIVE")).thenReturn(trades);

        List<Trade> result = tradeService.searchTradesByStatus("LIVE");
        assertEquals(1, result.size());
        verify(tradeRepository).findByTradeStatus("LIVE");
    }

    @Test
    void testSearchByDateRange() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        List<Trade> trades = Arrays.asList(new Trade());
        when(tradeRepository.findByTradeDateBetween(start, end)).thenReturn(trades);

        List<Trade> result = tradeService.searchTradesByDateRange(start, end);
        assertEquals(1, result.size());
        verify(tradeRepository).findByTradeDateBetween(start, end);
    }

    @Test
    void testGetTradesPaginated() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Trade> page = new PageImpl<>(Arrays.asList(new Trade()));
        when(tradeRepository.findAllActivePaginated(eq(pageable)))
                .thenReturn(page);

        Page<Trade> result = tradeService.getTradesPaginated(pageable);
        
        assertEquals(1, result.getTotalElements());
        verify(tradeRepository).findAllActivePaginated(eq(pageable));
    }

    @Test
    void testRsqlSearch() {
        List<Trade> trades = Arrays.asList(new Trade());
        when(tradeRepository.findByCounterpartyNameContaining("BigBank")).thenReturn(trades);

        List<Trade> result = tradeService.searchTradesRsql("counterparty.name==BigBank");
        
        assertEquals(1, result.size());
        verify(tradeRepository).findByCounterpartyNameContaining("BigBank");
    }

    @Test
    void testMultiCriteriaSearch() {
        List<Trade> allTrades = Arrays.asList(new Trade());
        when(tradeRepository.findAll()).thenReturn(allTrades);

        List<Trade> result = tradeService.searchTrades("BigBank", "RATES", "LIVE",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        
        assertNotNull(result);
        verify(tradeRepository).findAll();
    }
}