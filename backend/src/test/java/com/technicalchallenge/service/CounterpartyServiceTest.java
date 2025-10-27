package com.technicalchallenge.service;

import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.repository.CounterpartyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CounterpartyServiceTest {
    @Mock
    private CounterpartyRepository counterpartyRepository;
    @InjectMocks
    private CounterpartyService counterpartyService;

    @Test
    void testFindCounterpartyById() {
        Counterparty counterparty = new Counterparty();
        counterparty.setId(1L);
        when(counterpartyRepository.findById(1L)).thenReturn(Optional.of(counterparty));
        Optional<Counterparty> found = counterpartyService.getCounterpartyById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }
    // Add more tests for save, update, delete
}
