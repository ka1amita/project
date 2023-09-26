package com.gfa.controllers;

import static com.gfa.utils.Endpoint.REGISTER;
import static com.gfa.utils.Endpoint.CONFIRM_WITH_CODE;

import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.RegisterResponseDTO;
import com.gfa.models.AppUser;
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
import java.util.Optional;

@RestController
public class RegistrationController {

    private final AppUserService appUserService;

    private final MessageSource messsageSource;

    private final UserRestController userRestController;

    @Autowired
    public RegistrationController(AppUserService appUserService, MessageSource messsageSource) {
    public RegistrationController(AppUserService appUserService, UserRestController userRestController) {
        this.appUserService = appUserService;
        this.messsageSource = messsageSource;
        this.userRestController = userRestController;
    }

    @PostMapping(REGISTER)
    public ResponseEntity<? extends ResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) throws MessagingException {
        this.userRestController.store(registerRequest);
            return ResponseEntity.ok(new RegisterResponseDTO(messsageSource.getMessage("dto.register.response", null, LocaleContextHolder.getLocale())));
    }

    @GetMapping("/confirm/{activationCode}")
    public ResponseEntity<?> activateAccount(@PathVariable String activationCode) {
        String lang = appUserService.activateAccount(activationCode);
        return ResponseEntity.ok(new RegisterResponseDTO(messsageSource.getMessage("dto.activate.response", null, new Locale(lang))));
    }
}
