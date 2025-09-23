package com.technicalchallenge.service;

import com.technicalchallenge.model.UserPrivilege;
import com.technicalchallenge.repository.UserPrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserPrivilegeService {
    private static final Logger logger = LoggerFactory.getLogger(UserPrivilegeService.class);

    @Autowired
    private UserPrivilegeRepository userPrivilegeRepository;

    public List<UserPrivilege> getAllUserPrivileges() {
        logger.info("Retrieving all user privileges");
        return userPrivilegeRepository.findAll();
    }

    public Optional<UserPrivilege> getUserPrivilegeById(Long id) {
        logger.debug("Retrieving user privilege by id: {}", id);
        return userPrivilegeRepository.findById(id);
    }

    public UserPrivilege saveUserPrivilege(UserPrivilege userPrivilege) {
        logger.info("Saving user privilege: {}", userPrivilege);
        return userPrivilegeRepository.save(userPrivilege);
    }

    public void deleteUserPrivilege(Long id) {
        logger.warn("Deleting user privilege with id: {}", id);
        userPrivilegeRepository.deleteById(id);
    }
}
