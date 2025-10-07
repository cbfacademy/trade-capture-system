package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.CurrencyDTO;
import com.technicalchallenge.model.Currency;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {// Mapper for Currency and CurrencyDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert Currency entity to CurrencyDTO
    public CurrencyDTO toDto(Currency entity) {
        return modelMapper.map(entity, CurrencyDTO.class);
    }

    // Convert CurrencyDTO to Currency entity
    public Currency toEntity(CurrencyDTO dto) {
        return modelMapper.map(dto, Currency.class);
    }
}
