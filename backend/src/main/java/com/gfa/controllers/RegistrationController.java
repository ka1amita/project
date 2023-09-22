package com.gfa.controllers;

import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.RegisterResponseDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
public class RegistrationController {

    private final AppUserService appUserService;

    @Autowired
    public RegistrationController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<? extends ResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) throws MessagingException {
        appUserService.registerUser(registerRequest);
        return ResponseEntity.ok(new RegisterResponseDTO("Registration successful, please activate your account"));
    }

    @GetMapping("/confirm/{activationCode}")
    public ResponseEntity<? extends ResponseDTO> activateAccount(@PathVariable String activationCode) {
        appUserService.activateAccount(activationCode);
        return ResponseEntity.ok(new RegisterResponseDTO("Account activated successfully"));
    }
}
