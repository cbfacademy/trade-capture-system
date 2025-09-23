package com.technicalchallenge.controller;

import com.technicalchallenge.dto.CounterpartyDTO;
import com.technicalchallenge.mapper.CounterpartyMapper;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.service.CounterpartyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CounterpartyController.class)
public class CounterpartyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CounterpartyService counterpartyService;

    @MockBean
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
