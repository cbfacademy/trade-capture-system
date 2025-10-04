package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.TradeStatusDTO;
import com.technicalchallenge.model.TradeStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradeStatusMapper {// Mapper for TradeStatus and TradeStatusDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert TradeStatus entity to TradeStatusDTO
    public TradeStatusDTO toDto(TradeStatus entity) {
        return modelMapper.map(entity, TradeStatusDTO.class);
    }

    // Convert TradeStatusDTO to TradeStatus entity
    public TradeStatus toEntity(TradeStatusDTO dto) {
        return modelMapper.map(dto, TradeStatus.class);
    }
}
