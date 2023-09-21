package com.gfa.controllers;

import static com.gfa.utils.Endpoint.USERS;
import static org.springframework.http.HttpStatus.OK;

import com.gfa.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(USERS)
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
  // Pagination Example, check https://www.baeldung.com/spring-data-jpa-pagination-sorting
  @DeleteMapping("/list?page=1")
  public ResponseEntity<?> destroy(@RequestParam Integer page, @RequestParam Integer entries) {

    return ResponseEntity.status(OK).build();
  }


}
