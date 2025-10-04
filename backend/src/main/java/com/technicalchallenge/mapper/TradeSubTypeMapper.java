package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.TradeSubTypeDTO;
import com.technicalchallenge.model.TradeSubType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradeSubTypeMapper {// Mapper for TradeSubType and TradeSubTypeDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert TradeSubType entity to TradeSubTypeDTO
    public TradeSubTypeDTO toDto(TradeSubType entity) {
        return modelMapper.map(entity, TradeSubTypeDTO.class);
    }

    // Convert TradeSubTypeDTO to TradeSubType entity
    public TradeSubType toEntity(TradeSubTypeDTO dto) {
        return modelMapper.map(dto, TradeSubType.class);
    }
}
