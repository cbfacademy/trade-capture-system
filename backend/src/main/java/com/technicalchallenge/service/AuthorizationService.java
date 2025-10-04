package com.technicalchallenge.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorizationService {// Service class for user authentication

    // Injected ApplicationUserService for user operations
    private final ApplicationUserService applicationUserService;

    // Authenticate user with username and password
    public boolean authenticateUser(String userName, String password) {
        return applicationUserService.validateCredentials(userName, password);
    }
}
