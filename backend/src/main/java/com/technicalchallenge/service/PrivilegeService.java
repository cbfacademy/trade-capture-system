package com.technicalchallenge.service;

import com.technicalchallenge.model.Privilege;
import com.technicalchallenge.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class PrivilegeService {
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeService.class);

    @Autowired
    private PrivilegeRepository privilegeRepository;

    public List<Privilege> getAllPrivileges() {
        logger.info("Retrieving all privileges");
        return privilegeRepository.findAll();
    }

    public Optional<Privilege> getPrivilegeById(Long id) {
        logger.debug("Retrieving privilege by id: {}", id);
        return privilegeRepository.findById(id);
    }

    public Privilege savePrivilege(Privilege privilege) {
        logger.info("Saving privilege: {}", privilege);
        return privilegeRepository.save(privilege);
    }

    public void deletePrivilege(Long id) {
        logger.warn("Deleting privilege with id: {}", id);
        privilegeRepository.deleteById(id);
    }
}
