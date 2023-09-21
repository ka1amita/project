package com.gfa.controllers;

import static com.gfa.utils.Endpoint.RESET_PASSWORD;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RESET_PASSWORD)
public class PasswordResetController {
    @Autowired
    private final AppUserService appUserService;

    public PasswordResetController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<?> index(@RequestBody(required = false) PasswordResetRequestDTO passwordResetRequestDTO) throws MessagingException {
        return appUserService.reset(passwordResetRequestDTO);
    }

    @PostMapping("/{resetCode}")
    public ResponseEntity<?> passwordResetWithCode(@RequestBody PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, @PathVariable String resetCode) {
        return appUserService.resetWithCode(passwordResetWithCodeRequestDTO, resetCode);
    }
}
