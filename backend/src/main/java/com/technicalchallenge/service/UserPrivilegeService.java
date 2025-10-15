package com.technicalchallenge.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technicalchallenge.model.UserPrivilege;
import com.technicalchallenge.model.UserPrivilegeId;
import com.technicalchallenge.repository.UserPrivilegeRepository;

@Service
public class UserPrivilegeService {
    private static final Logger logger = LoggerFactory.getLogger(UserPrivilegeService.class);

    @Autowired
    private UserPrivilegeRepository userPrivilegeRepository;

    public List<UserPrivilege> getAllUserPrivileges() {
        logger.info("Retrieving all user privileges");
        return userPrivilegeRepository.findAll();
    }

    public List<UserPrivilege> getUserPrivileges(Long userId) {
        logger.debug("Retrieving privileges for user: {}", userId);
        return userPrivilegeRepository.findByUserId(userId);
    }

    public UserPrivilege saveUserPrivilege(UserPrivilege userPrivilege) {
        logger.info("Saving user privilege: {userId={}, privilegeId={}}", userPrivilege.getUserId(), userPrivilege.getPrivilegeId());
        return userPrivilegeRepository.save(userPrivilege);
    }

    public void deleteUserPrivilege(UserPrivilegeId id) {
        logger.warn("Deleting user privilege: {userId={}, privilegeId={}}", id.getUserId(), id.getPrivilegeId());
        userPrivilegeRepository.deleteById(id);
    }
}
