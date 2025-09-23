package com.technicalchallenge.service;

import com.technicalchallenge.model.UserProfile;
import com.technicalchallenge.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public Optional<UserProfile> updateUserProfile(Long id, UserProfile updatedProfile) {
        return userProfileRepository.findById(id).map(existingProfile -> {
            existingProfile.setUserType(updatedProfile.getUserType());
            return userProfileRepository.save(existingProfile);
        });
    }

    public boolean deleteUserProfile(Long id) {
        if (userProfileRepository.existsById(id)) {
            userProfileRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
