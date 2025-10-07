package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.DeskDTO;
import com.technicalchallenge.model.Desk;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeskMapper {// Mapper for Desk and DeskDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert Desk entity to DeskDTO
    public DeskDTO toDto(Desk entity) {
        return modelMapper.map(entity, DeskDTO.class);
    }

    // Convert DeskDTO to Desk entity
    public Desk toEntity(DeskDTO dto) {
        return modelMapper.map(dto, Desk.class);
    }
}
