package com.technicalchallenge.applicationuser;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.technicalchallenge.userprofile.UserProfile;
import com.technicalchallenge.userprofile.UserProfileRepository;

@Component
public class ApplicationUserMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public ApplicationUserDTO toDto(ApplicationUser entity) {
        ApplicationUserDTO dto = modelMapper.map(entity, ApplicationUserDTO.class);
        if (entity.getUserProfile() != null) {
            dto.setUserProfile(entity.getUserProfile().getUserType());
        }
        return dto;
    }

    public ApplicationUser toEntity(ApplicationUserDTO dto) {
        ApplicationUser entity = modelMapper.map(dto, ApplicationUser.class);
        if (dto.getUserProfile() != null) {
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserType(dto.getUserProfile());
            userProfileOpt.ifPresent(entity::setUserProfile);
        }
        return entity;
    }
}

