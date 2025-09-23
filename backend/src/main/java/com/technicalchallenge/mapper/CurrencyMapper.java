package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.CurrencyDTO;
import com.technicalchallenge.model.Currency;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {
    @Autowired
    private ModelMapper modelMapper;

    public CurrencyDTO toDto(Currency entity) {
        return modelMapper.map(entity, CurrencyDTO.class);
    }

    public Currency toEntity(CurrencyDTO dto) {
        return modelMapper.map(dto, Currency.class);
    }
}
