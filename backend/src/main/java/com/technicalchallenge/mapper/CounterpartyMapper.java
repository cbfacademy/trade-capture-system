package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.CounterpartyDTO;
import com.technicalchallenge.model.Counterparty;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CounterpartyMapper {
    @Autowired
    private ModelMapper modelMapper;

    public CounterpartyDTO toDto(Counterparty entity) {
        return modelMapper.map(entity, CounterpartyDTO.class);
    }

    public Counterparty toEntity(CounterpartyDTO dto) {
        return modelMapper.map(dto, Counterparty.class);
    }
}
