package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.CounterpartyDTO;
import com.technicalchallenge.model.Counterparty;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CounterpartyMapper {// Mapper for Counterparty and CounterpartyDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert Counterparty entity to CounterpartyDTO
    public CounterpartyDTO toDto(Counterparty entity) {
        return modelMapper.map(entity, CounterpartyDTO.class);
    }

    // Convert CounterpartyDTO to Counterparty entity
    public Counterparty toEntity(CounterpartyDTO dto) {
        return modelMapper.map(dto, Counterparty.class);
    }
}
