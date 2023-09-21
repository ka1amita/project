package com.gfa.controllers;

import static com.gfa.utils.Endpoint.REGISTER;
import static com.gfa.utils.Endpoint.CONFIRM_WITH_CODE;

import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.RegisterResponseDTO;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
public class RegistrationController {

    private final AppUserService appUserService;

    @Autowired
    public RegistrationController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping(REGISTER)
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequest) throws MessagingException {
        appUserService.registerUser(registerRequest);
        return ResponseEntity.ok(new RegisterResponseDTO("Registration successful, please activate your account!"));
    }

    @GetMapping(CONFIRM_WITH_CODE + "/{activationCode}")
    public ResponseEntity<?> activateAccount(@PathVariable String activationCode) {
        appUserService.activateAccount(activationCode);
        return ResponseEntity.ok(new RegisterResponseDTO("Account activated successfully!"));
    }
}
