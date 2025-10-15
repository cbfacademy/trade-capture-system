package com.technicalchallenge.tradestatus;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradeStatusMapper {
    @Autowired
    private ModelMapper modelMapper;

    public TradeStatusDTO toDto(TradeStatus entity) {
        return modelMapper.map(entity, TradeStatusDTO.class);
    }

    public TradeStatus toEntity(TradeStatusDTO dto) {
        return modelMapper.map(dto, TradeStatus.class);
    }
}
