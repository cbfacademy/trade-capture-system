package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.DeskDTO;
import com.technicalchallenge.model.Desk;
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
