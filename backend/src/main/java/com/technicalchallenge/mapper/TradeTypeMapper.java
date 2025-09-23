package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.TradeTypeDTO;
import com.technicalchallenge.model.TradeType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradeTypeMapper {
    @Autowired
    private ModelMapper modelMapper;

    public TradeTypeDTO toDto(TradeType entity) {
        return modelMapper.map(entity, TradeTypeDTO.class);
    }

    public TradeType toEntity(TradeTypeDTO dto) {
        return modelMapper.map(dto, TradeType.class);
    }
}
