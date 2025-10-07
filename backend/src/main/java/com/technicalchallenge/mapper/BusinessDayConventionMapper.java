package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.BusinessDayConventionDTO;
import com.technicalchallenge.model.BusinessDayConvention;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessDayConventionMapper {// Mapper for BusinessDayConvention and BusinessDayConventionDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert BusinessDayConvention entity to BusinessDayConventionDTO
    public BusinessDayConventionDTO toDto(BusinessDayConvention entity) {
        return modelMapper.map(entity, BusinessDayConventionDTO.class);
    }

    // Convert BusinessDayConventionDTO to BusinessDayConvention entity
    public BusinessDayConvention toEntity(BusinessDayConventionDTO dto) {
        return modelMapper.map(dto, BusinessDayConvention.class);
    }
}
