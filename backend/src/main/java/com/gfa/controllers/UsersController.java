package com.gfa.controllers;

import com.gfa.models.AppUser;
import com.gfa.services.AppUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UsersController {

  public final AppUserService appUserService;

  @Autowired
  public UsersController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @PostMapping("/list")
  ResponseEntity<List<AppUser>> getAllAppUsers() {
    return ResponseEntity.ok(appUserService.getAllAppUsers());
  }
}
