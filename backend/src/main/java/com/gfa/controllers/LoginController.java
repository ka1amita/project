package com.gfa.controllers;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    private final AppUserService appUserService;

    @Autowired
    public LoginController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody Optional<LoginRequestDTO> loginRequestDto) {
        return ResponseEntity.ok(appUserService.userLogin(loginRequestDto));
    }
}
