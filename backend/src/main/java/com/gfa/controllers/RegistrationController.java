package com.gfa.controllers;

import static com.gfa.utils.Endpoint.REGISTER;

import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.RegisterResponseDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Locale;

@RestController
public class RegistrationController {

    private final AppUserService appUserService;

    private final MessageSource messageSource;

    private final UserRestController userRestController;

    @Autowired
    public RegistrationController(AppUserService appUserService, UserRestController userRestController,MessageSource messageSource) {
        this.appUserService = appUserService;
        this.messageSource = messageSource;
        this.userRestController = userRestController;
    }

    @PostMapping(REGISTER)
    public ResponseEntity<? extends ResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) throws MessagingException {
        this.userRestController.store(registerRequest);
            return ResponseEntity.ok(new RegisterResponseDTO(messageSource.getMessage("dto.register.response", null, LocaleContextHolder.getLocale())));
    }

    @GetMapping("/confirm/{activationCode}")
    public ResponseEntity<? extends ResponseDTO> activateAccount(@PathVariable String activationCode) {
        String lang = appUserService.activateAccount(activationCode);
        return ResponseEntity.ok(new RegisterResponseDTO(messageSource.getMessage("dto.activate.response", null, new Locale(lang))));
    }
}
