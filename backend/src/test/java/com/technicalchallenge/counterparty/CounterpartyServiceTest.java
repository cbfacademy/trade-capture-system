package com.technicalchallenge.counterparty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
