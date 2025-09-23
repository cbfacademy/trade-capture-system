package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.UserDTO;
import com.technicalchallenge.model.ApplicationUser;
import com.technicalchallenge.model.UserProfile;
import com.technicalchallenge.repository.UserProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationUserMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserDTO toDto(ApplicationUser entity) {
        UserDTO dto = modelMapper.map(entity, UserDTO.class);
        if (entity.getUserProfile() != null) {
            dto.setUserProfile(entity.getUserProfile().getUserType());
        }
        return dto;
    }

    public ApplicationUser toEntity(UserDTO dto) {
        ApplicationUser entity = modelMapper.map(dto, ApplicationUser.class);
        if (dto.getUserProfile() != null) {
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserType(dto.getUserProfile());
            userProfileOpt.ifPresent(entity::setUserProfile);
        }
        return entity;
    }
}

