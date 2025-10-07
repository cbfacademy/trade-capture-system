package com.technicalchallenge.controller;

import com.technicalchallenge.service.AuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@Validated
@AllArgsConstructor
public class AuthorizationController {// Purpose of this controller - to authenticate users based on their username and an authorization token.

    private final AuthorizationService authorizationService;// Service to handle authentication logic


    @PostMapping("/{userName}") // Endpoint to handle login requests
    public ResponseEntity<?> login(@PathVariable(name = "userName") String userName, @RequestParam(name = "Authorization") String authorization) {
        // Authenticate user and return appropriate response
        return authorizationService.authenticateUser(userName, authorization) ?
                ResponseEntity.ok("Login successful") :
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login failed");
    }
}