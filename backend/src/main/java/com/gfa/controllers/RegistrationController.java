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

@RestController
public class RegistrationController {

    private final AppUserService appUserService;

    @Autowired
    public RegistrationController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequest) {

        if (registerRequest.getUsername() == null || registerRequest.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be null or empty");
        }

        if (registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty()){
            return ResponseEntity.badRequest().body("Email cannot be null or empty");
        }

        if (registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty()){
            return ResponseEntity.badRequest().body("Password cannot be null or empty");
        }

        try {
            appUserService.registerUser(registerRequest);
            return ResponseEntity.ok(new RegisterResponseDTO("Registration successful, please activate your account!"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Username already exists.");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Email already exists.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again.");
        }

    }

    @GetMapping("/confirm/{activationCode}")
    public ResponseEntity<?> activateAccount(@PathVariable String activationCode) {
        try {
            appUserService.activateAccount(activationCode);
            return ResponseEntity.ok(new RegisterResponseDTO("Account activated successfully!"));
        } catch (InvalidActivationCodeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again.");
        }
    }

}
