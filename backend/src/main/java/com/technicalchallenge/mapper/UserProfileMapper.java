package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.UserProfileDTO;
import com.technicalchallenge.model.UserProfile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {
    @Autowired
    private ModelMapper modelMapper;

    public UserProfileDTO toDto(UserProfile entity) {
        return modelMapper.map(entity, UserProfileDTO.class);
    }

    public UserProfile toEntity(UserProfileDTO dto) {
        return modelMapper.map(dto, UserProfile.class);
    }
}

