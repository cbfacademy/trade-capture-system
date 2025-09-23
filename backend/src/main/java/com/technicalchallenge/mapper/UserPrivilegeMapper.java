package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.UserPrivilegeDTO;
import com.technicalchallenge.model.UserPrivilege;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPrivilegeMapper {
    @Autowired
    private ModelMapper modelMapper;

    public UserPrivilegeDTO toDto(UserPrivilege entity) {
        return modelMapper.map(entity, UserPrivilegeDTO.class);
    }

    public UserPrivilege toEntity(UserPrivilegeDTO dto) {
        return modelMapper.map(dto, UserPrivilege.class);
    }
}
