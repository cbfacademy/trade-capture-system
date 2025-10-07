package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.UserProfileDTO;
import com.technicalchallenge.model.UserProfile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {// Mapper for UserProfile and UserProfileDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert UserProfile entity to UserProfileDTO
    public UserProfileDTO toDto(UserProfile entity) {
        return modelMapper.map(entity, UserProfileDTO.class);
    }

    // Convert UserProfileDTO to UserProfile entity
    public UserProfile toEntity(UserProfileDTO dto) {
        return modelMapper.map(dto, UserProfile.class);
    }
}

