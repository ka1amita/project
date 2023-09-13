package com.gfa.controllers;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordResetController {
    @Autowired
    private final AppUserService appUserService;

    public PasswordResetController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetRequest(@RequestBody(required = false) PasswordResetRequestDTO passwordResetRequestDTO) {
        return appUserService.reset(passwordResetRequestDTO);
    }

    @PostMapping("/reset/{resetCode}")
    public ResponseEntity<?> resetWithCode(@RequestBody PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, @PathVariable String resetCode) {
        return appUserService.resetWithCode(passwordResetWithCodeRequestDTO, resetCode);
    }
}
