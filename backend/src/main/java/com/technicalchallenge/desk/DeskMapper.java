package com.technicalchallenge.desk;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeskMapper {
    @Autowired
    private ModelMapper modelMapper;

    public DeskDTO toDto(Desk entity) {
        return modelMapper.map(entity, DeskDTO.class);
    }

    public Desk toEntity(DeskDTO dto) {
        return modelMapper.map(dto, Desk.class);
    }
}
