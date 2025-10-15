package com.technicalchallenge.authorization;

import org.springframework.stereotype.Service;

import com.technicalchallenge.applicationuser.ApplicationUserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthorizationService {

    private final ApplicationUserService applicationUserService;

    public boolean authenticateUser(String userName, String password) {
        return applicationUserService.validateCredentials(userName, password);
    }
}
