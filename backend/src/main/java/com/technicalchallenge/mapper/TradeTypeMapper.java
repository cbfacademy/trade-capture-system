package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.TradeTypeDTO;
import com.technicalchallenge.model.TradeType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradeTypeMapper {// Mapper for TradeType and TradeTypeDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert TradeType entity to TradeTypeDTO
    public TradeTypeDTO toDto(TradeType entity) {
        return modelMapper.map(entity, TradeTypeDTO.class);
    }

    // Convert TradeTypeDTO to TradeType entity
    public TradeType toEntity(TradeTypeDTO dto) {
        return modelMapper.map(dto, TradeType.class);
    }
}
