package com.gfa.controllers;

import com.gfa.dtos.responsedtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.RegisterResponseDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private final AppUserService appUserService;

    @Autowired
    public RegistrationController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        appUserService.registerUser(registerRequest);
        return ResponseEntity.ok(new RegisterResponseDTO("Registration successful, please activate your account!"));
    }

    @GetMapping("/confirm/{activationCode}")
    public ResponseEntity<?> activateAccount(@PathVariable String activationCode) {
        appUserService.activateAccount(activationCode);
        return ResponseEntity.ok(new RegisterResponseDTO("Account activated successfully!"));
    }

}
