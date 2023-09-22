package com.gfa.controllers;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/reset")
public class PasswordResetController {
    @Autowired
    private final AppUserService appUserService;

    public PasswordResetController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<? extends ResponseDTO> index(@RequestBody(required = false) PasswordResetRequestDTO passwordResetRequestDTO) throws MessagingException {
        return appUserService.reset(passwordResetRequestDTO);
    }

    @PostMapping("/{resetCode}")
    public ResponseEntity<? extends ResponseDTO> passwordResetWithCode(@RequestBody PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, @PathVariable String resetCode) {
        return appUserService.resetWithCode(passwordResetWithCodeRequestDTO, resetCode);
    }
}
