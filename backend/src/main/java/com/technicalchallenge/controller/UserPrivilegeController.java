package com.technicalchallenge.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technicalchallenge.dto.UserPrivilegeDTO;
import com.technicalchallenge.mapper.UserPrivilegeMapper;
import com.technicalchallenge.model.UserPrivilege;
import com.technicalchallenge.model.UserPrivilegeId;
import com.technicalchallenge.service.UserPrivilegeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/userPrivileges")
public class UserPrivilegeController {
    private static final Logger logger = LoggerFactory.getLogger(UserPrivilegeController.class);

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @Autowired
    private UserPrivilegeMapper userPrivilegeMapper;

    @GetMapping()
    public List<UserPrivilegeDTO> getAllUserPrivileges() {
        logger.info("Fetching all user privileges");
        return userPrivilegeService.getAllUserPrivileges().stream()
                .map(userPrivilegeMapper::toDto)
                .toList();
    }

    @GetMapping("/{userId}")
    public List<UserPrivilegeDTO> getUserPrivileges(@PathVariable Long userId) {
        logger.debug("Fetching privileges for user: {}", userId);
        return userPrivilegeService.getUserPrivileges(userId).stream()
                .map(userPrivilegeMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<UserPrivilegeDTO> createUserPrivilege(@Valid @RequestBody UserPrivilegeDTO userPrivilegeDTO) {
        logger.info("Creating new user privilege: {}", userPrivilegeDTO);
        UserPrivilege createdUserPrivilege = userPrivilegeService.saveUserPrivilege(userPrivilegeMapper.toEntity(userPrivilegeDTO));
        URI location = URI.create(String.format("/api/users/%d/%d",
                createdUserPrivilege.getUserId(), createdUserPrivilege.getPrivilegeId()));
        return ResponseEntity.created(location)
                .body(userPrivilegeMapper.toDto(createdUserPrivilege));
    }

    @DeleteMapping("/{userId}/{privilegeId}")
    public ResponseEntity<Void> deleteUserPrivilege(@PathVariable Long userId, @PathVariable Long privilegeId) {
        UserPrivilegeId id = new UserPrivilegeId(userId, privilegeId);
        logger.warn("Deleting user privilege with id: {userId={}, privilegeId={}}", userId, privilegeId);
        userPrivilegeService.deleteUserPrivilege(id);
        return ResponseEntity.noContent().build();
    }
}
