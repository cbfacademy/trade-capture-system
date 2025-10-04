package com.technicalchallenge.controller;

import com.technicalchallenge.dto.PrivilegeDTO;
import com.technicalchallenge.mapper.PrivilegeMapper;
import com.technicalchallenge.model.Privilege;
import com.technicalchallenge.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/privileges")
public class PrivilegeController {// Purpose of this controller - to manage privileges, including creating, retrieving, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);

    // Injecting PrivilegeService to handle business logic
    @Autowired
    private PrivilegeService privilegeService;

    // Mapper to convert between entity and DTO
    @Autowired
    private PrivilegeMapper privilegeMapper;

    // Endpoint to retrieve all privileges
    @GetMapping
    public List<PrivilegeDTO> getAllPrivileges() {
        logger.info("Fetching all privileges");
        return privilegeService.getAllPrivileges().stream()
                .map(privilegeMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve a privilege by its ID
    @GetMapping("/{id}")
    public ResponseEntity<PrivilegeDTO> getPrivilegeById(@PathVariable Long id) {
        logger.debug("Fetching privilege by id: {}", id);
        Optional<Privilege> privilege = privilegeService.getPrivilegeById(id);
        return privilege.map(privilegeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a new privilege
    @PostMapping
    public ResponseEntity<PrivilegeDTO> createPrivilege(@Valid @RequestBody PrivilegeDTO privilegeDTO) {
        logger.info("Creating new privilege: {}", privilegeDTO);
        Privilege savedPrivilege = privilegeService.savePrivilege(privilegeMapper.toEntity(privilegeDTO));
        return ResponseEntity.created(URI.create("/api/privileges/" + savedPrivilege.getId())).body(privilegeMapper.toDto(savedPrivilege));
    }

    // Endpoint to delete a privilege by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrivilege(@PathVariable Long id) {
        logger.warn("Deleting privilege with id: {}", id);
        privilegeService.deletePrivilege(id);
        return ResponseEntity.noContent().build();
    }
}
