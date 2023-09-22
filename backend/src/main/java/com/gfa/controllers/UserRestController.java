package com.gfa.controllers;

import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRestController {

  public final AppUserService appUserService;

  @Autowired
  public UserRestController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> destroy(@PathVariable Long id) {
    appUserService.removeAppUser(id);
    return ResponseEntity.status(201).build();
  }
}