package com.technicalchallenge.controller;

import com.technicalchallenge.dto.UserProfileDTO;
import com.technicalchallenge.mapper.UserProfileMapper;
import com.technicalchallenge.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userProfiles")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileMapper userProfileMapper;

    @GetMapping
    public List<UserProfileDTO> getAllUserProfiles() {
        return userProfileService.getAllUserProfiles().stream()
                .map(userProfileMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfileById(@PathVariable Long id) {
        return userProfileService.getUserProfileById(id)
                .map(userProfileMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserProfileDTO createUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        return userProfileMapper.toDto(userProfileService.saveUserProfile(userProfileMapper.toEntity(userProfileDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDTO userProfileDTO) {
        return userProfileService.updateUserProfile(id, userProfileMapper.toEntity(userProfileDTO))
                .map(userProfile -> ResponseEntity.ok(userProfileMapper.toDto(userProfile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        if (userProfileService.deleteUserProfile(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
