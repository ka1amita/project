package com.gfa.controllers;

import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.RegisterResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@RestController
public class RegistrationController {

    private final AppUserService appUserService;

    private final MessageSource messsageSource;


    @Autowired
    public RegistrationController(AppUserService appUserService, MessageSource messsageSource) {
        this.appUserService = appUserService;
        this.messsageSource = messsageSource;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) throws MessagingException {
        appUserService.registerUser(registerRequest);
        return ResponseEntity.ok(new RegisterResponseDTO(messsageSource.getMessage("dto.register.response", null, LocaleContextHolder.getLocale())));
    }

    @GetMapping("/confirm/{activationCode}")
    public ResponseEntity<?> activateAccount(@PathVariable String activationCode) {
        String lang = appUserService.activateAccount(activationCode);
        return ResponseEntity.ok(new RegisterResponseDTO(messsageSource.getMessage("dto.activate.response", null, new Locale(lang))));
    }
}
