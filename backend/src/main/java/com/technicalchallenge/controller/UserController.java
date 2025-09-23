package com.technicalchallenge.controller;

import com.technicalchallenge.dto.UserDTO;
import com.technicalchallenge.mapper.ApplicationUserMapper;
import com.technicalchallenge.model.ApplicationUser;
import com.technicalchallenge.model.TradeType;
import com.technicalchallenge.service.ApplicationUserService;
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
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private ApplicationUserMapper applicationUserMapper;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return applicationUserService.getAllUsers().stream()
                .map(applicationUserMapper::toDto)
                .toList();
    }

    @GetMapping("/loginId/{loginId}")
    public ResponseEntity<UserDTO> getUserByLoginId(@PathVariable("loginId") String loginId) {
        logger.debug("Fetching user by loginId: {}", loginId);
        Optional<ApplicationUser> user = applicationUserService.getAllUsers().stream()
                .filter(u -> u.getLoginId().equals(loginId))
                .findFirst();

        return user.map(applicationUserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.debug("Fetching user by id: {}", id);
        return applicationUserService.getUserById(id)
                .map(applicationUserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDto) {
        logger.info("Creating new user: {}", userDto);
        ApplicationUser user = applicationUserMapper.toEntity(userDto);
        ApplicationUser savedUser = applicationUserService.saveUser(user);
        UserDTO savedUserDto = applicationUserMapper.toDto(savedUser);
        return ResponseEntity.created(URI.create("/api/users/" + savedUserDto.getId())).body(savedUserDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(name = "id") Long id, @Valid @RequestBody UserDTO userDto) {
        logger.info("Updating user with id: {}", id);
        ApplicationUser user = applicationUserMapper.toEntity(userDto);
        ApplicationUser updatedUser = applicationUserService.updateUser(id, user);
        UserDTO updatedUserDto = applicationUserMapper.toDto(updatedUser);
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.warn("Deleting user with id: {}", id);
        applicationUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/values")
    public List<String> getAllTradeTypeValues() {
        logger.info("Fetching all trade type values");
        return applicationUserService.getAllUsers().stream()
                .map(ApplicationUser::getLoginId)
                .toList();
    }
}
