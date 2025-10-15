package com.technicalchallenge.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.technicalchallenge.dto.PrivilegeDTO;
import com.technicalchallenge.model.Privilege;

@Component
public class PrivilegeMapper {
    @Autowired
    private ModelMapper modelMapper;

    public PrivilegeDTO toDto(Privilege entity) {
        return modelMapper.map(entity, PrivilegeDTO.class);
    }

    public Privilege toEntity(PrivilegeDTO dto) {
        return modelMapper.map(dto, Privilege.class);
    }
}
