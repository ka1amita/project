package com.gfa.controllers;

import com.gfa.dtos.requestdtos.UpdateAppUserDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.services.AppUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public final AppUserService appUserService;

    @Autowired
    public UserRestController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/")
    public ResponseEntity<List<? extends ResponseDTO>> index() {
        return ResponseEntity.ok(appUserService.getAllAppUsers());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<? extends ResponseDTO>> indexDeleted() {
        return ResponseEntity.ok(appUserService.getAllAppUsersDeleted());
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ResponseDTO> show(@PathVariable Long id) {
        return ResponseEntity.ok(appUserService.fetchUserApi(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends ResponseDTO> update(@RequestBody(required = false) UpdateAppUserDTO request,
                                                      @PathVariable Long id) throws MessagingException {
        return ResponseEntity.ok(appUserService.updateAppUserApi(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends ResponseDTO> destroy(@PathVariable Long id) {
        appUserService.removeAppUser(id);
        return ResponseEntity.status(201).build();
    }
}
