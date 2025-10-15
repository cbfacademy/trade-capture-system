package com.technicalchallenge.applicationuser;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management and authentication operations")
public class ApplicationUserController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationUserController.class);

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private ApplicationUserMapper applicationUserMapper;

    @GetMapping
    @Operation(summary = "Get all users",
               description = "Retrieves a list of all users in the system with their profile information and privileges")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all users",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ApplicationUserDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<ApplicationUserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return applicationUserService.getAllUsers().stream()
                .map(applicationUserMapper::toDto)
                .toList();
    }

    @GetMapping("/loginId/{loginId}")
    @Operation(summary = "Get user by login ID",
               description = "Retrieves a specific user by their unique login identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found and returned successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ApplicationUserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid login ID format")
    })
    public ResponseEntity<ApplicationUserDTO> getUserByLoginId(
            @Parameter(description = "Unique login identifier of the user", required = true)
            @PathVariable String loginId) {
        logger.debug("Fetching user by loginId: {}", loginId);
        Optional<ApplicationUser> applicationUser = applicationUserService.getAllUsers().stream()
                .filter(u -> u.getLoginId().equals(loginId))
                .findFirst();

        return applicationUser.map(applicationUserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID",
               description = "Retrieves a specific user by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found and returned successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ApplicationUserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID format")
    })
    public ResponseEntity<ApplicationUserDTO> getUserById(@PathVariable Long id) {
        logger.debug("Fetching user by id: {}", id);
        return applicationUserService.getUserById(id)
                .map(applicationUserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new user",
               description = "Registers a new user in the system with the provided profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ApplicationUserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user data provided"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApplicationUserDTO> createUser(@Valid @RequestBody ApplicationUserDTO userDto) {
        logger.info("Creating new user: {}", userDto);
        ApplicationUser applicationUser = applicationUserMapper.toEntity(userDto);
        ApplicationUser savedUser = applicationUserService.saveUser(applicationUser);
        ApplicationUserDTO savedUserDto = applicationUserMapper.toDto(savedUser);
        return ResponseEntity.created(URI.create("/api/users/" + savedUserDto.getId())).body(savedUserDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user",
               description = "Updates the profile information of an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ApplicationUserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid user data provided or user ID format")
    })
    public ResponseEntity<ApplicationUserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody ApplicationUserDTO userDto) {
        logger.info("Updating user with id: {}", id);
        ApplicationUser applicationUser = applicationUserMapper.toEntity(userDto);
        ApplicationUser updatedUser = applicationUserService.updateUser(id, applicationUser);
        ApplicationUserDTO updatedUserDto = applicationUserMapper.toDto(updatedUser);
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user",
               description = "Removes a user from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.warn("Deleting user with id: {}", id);
        applicationUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/values")
    @Operation(summary = "Get all trade type values",
               description = "Retrieves a list of all trade type values associated with users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all trade type values",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<String> getAllTradeTypeValues() {
        logger.info("Fetching all trade type values");
        return applicationUserService.getAllUsers().stream()
                .map(ApplicationUser::getLoginId)
                .toList();
    }
}
