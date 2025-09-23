package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.LegTypeDTO;
import com.technicalchallenge.model.LegType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LegTypeMapper {
    @Autowired
    private ModelMapper modelMapper;

    public LegTypeDTO toDto(LegType entity) {
        return modelMapper.map(entity, LegTypeDTO.class);
    }

    public LegType toEntity(LegTypeDTO dto) {
        return modelMapper.map(dto, LegType.class);
    }
}
