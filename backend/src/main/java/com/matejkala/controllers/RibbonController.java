package com.matejkala.controllers;

import com.matejkala.utils.Endpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(Endpoint.RIBBON)
@RestController
public class RibbonController {
    @GetMapping
    public ResponseEntity<?> sendRibbonData() {
        return ResponseEntity.ok().build();
    }
}
