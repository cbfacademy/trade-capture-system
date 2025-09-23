package com.technicalchallenge.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorizationService {

    private final ApplicationUserService applicationUserService;

    public boolean authenticateUser(String userName, String password) {
        return applicationUserService.validateCredentials(userName, password);
    }
}
