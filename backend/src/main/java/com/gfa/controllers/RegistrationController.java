package com.gfa.controllers;

import com.gfa.dtos.responsedtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.RegisterResponseDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequest) {

        try {
            userService.registerUser(registerRequest);
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
            userService.activateAccount(activationCode);
            return ResponseEntity.ok(new RegisterResponseDTO("Account activated successfully!"));
        } catch (InvalidActivationCodeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again.");
        }
    }

}
