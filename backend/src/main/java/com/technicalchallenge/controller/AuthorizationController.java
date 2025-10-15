package com.technicalchallenge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.technicalchallenge.service.AuthorizationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/login")
@Validated
@AllArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/{userName}")
    public ResponseEntity<?> login(@PathVariable String userName, @RequestParam(name = "Authorization") String authorization) {

        return authorizationService.authenticateUser(userName, authorization) ?
                ResponseEntity.ok("Login successful") :
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login failed");
    }
}