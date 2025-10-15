package com.technicalchallenge.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.technicalchallenge.dto.BusinessDayConventionDTO;
import com.technicalchallenge.model.BusinessDayConvention;

@Component
public class BusinessDayConventionMapper {
    @Autowired
    private ModelMapper modelMapper;

    public BusinessDayConventionDTO toDto(BusinessDayConvention entity) {
        return modelMapper.map(entity, BusinessDayConventionDTO.class);
    }

    public BusinessDayConvention toEntity(BusinessDayConventionDTO dto) {
        return modelMapper.map(dto, BusinessDayConvention.class);
    }
}
