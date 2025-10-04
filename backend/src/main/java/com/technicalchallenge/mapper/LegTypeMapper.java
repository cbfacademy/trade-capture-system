package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.LegTypeDTO;
import com.technicalchallenge.model.LegType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LegTypeMapper {// Mapper for LegType and LegTypeDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert LegType entity to LegTypeDTO
    public LegTypeDTO toDto(LegType entity) {
        return modelMapper.map(entity, LegTypeDTO.class);
    }

    // Convert LegTypeDTO to LegType entity
    public LegType toEntity(LegTypeDTO dto) {
        return modelMapper.map(dto, LegType.class);
    }
}
