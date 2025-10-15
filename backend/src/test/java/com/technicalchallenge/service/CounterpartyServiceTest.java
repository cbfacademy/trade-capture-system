package com.technicalchallenge.service;


import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.repository.CounterpartyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
 // Added tests for save, update, delete
    @Test
    void testSaveCounterparty() {
        Counterparty counterparty = new Counterparty();
        counterparty.setId(2L);
        
        when(counterpartyRepository.save(any(Counterparty.class))).thenReturn(counterparty);
        Counterparty saved = counterpartyService.saveCounterparty(counterparty);
        assertNotNull(saved);
        assertEquals(2L, saved.getId());
        verify(counterpartyRepository, times(1)).save(counterparty);
    }

        @Test
        void testUpdateCounterparty() {
            Counterparty existing = new Counterparty();
            existing.setId(3L);
            existing.setName("Old Name");

            Counterparty updateData = new Counterparty();
            updateData.setName("New Name");

            when(counterpartyRepository.findById(3L)).thenReturn(Optional.of(existing));
            when(counterpartyRepository.save(existing)).thenReturn(existing);

            Counterparty result = counterpartyService.updateCounterparty(3L, updateData);

            assertEquals("New Name", result.getName());
            verify(counterpartyRepository, times(1)).findById(3L);
            verify(counterpartyRepository, times(1)).save(existing);
        }



    @Test
    void testDeleteCounterparty() {
        Long counterpartyId = 4L;
        doNothing().when(counterpartyRepository).deleteById(counterpartyId);
        counterpartyService.deleteCounterparty(counterpartyId);
        verify(counterpartyRepository, times(1)).deleteById(counterpartyId);
    }

   
}
