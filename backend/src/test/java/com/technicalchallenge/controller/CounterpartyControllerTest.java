package com.technicalchallenge.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.technicalchallenge.dto.CounterpartyDTO;
import com.technicalchallenge.mapper.CounterpartyMapper;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.service.CounterpartyService;

@WebMvcTest(CounterpartyController.class)
public class CounterpartyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CounterpartyService counterpartyService;

    @MockitoBean
    private CounterpartyMapper counterpartyMapper;

    @BeforeEach
    public void setup() {
        Counterparty counterparty = new Counterparty();
        counterparty.setId(1L);
        counterparty.setName("Counterparty 1");
        counterparty.setAddress("Address 1");

        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        counterpartyDTO.setId(counterparty.getId());
        counterpartyDTO.setName(counterparty.getName());
        counterpartyDTO.setAddress(counterparty.getAddress());
        when(counterpartyService.getAllCounterparties()).thenReturn(List.of(counterparty));
        when(counterpartyMapper.toDto(counterparty)).thenReturn(counterpartyDTO);
        when(counterpartyMapper.toEntity(counterpartyDTO)).thenReturn(counterparty);
    }

    @Test
    void shouldReturnAllCounterparties() throws Exception {
        mockMvc.perform(get("/api/counterparties"))
                .andExpect(status().isOk());
    }
    // Add more tests for POST, PUT, DELETE as needed
}
