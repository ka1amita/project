package com.gfa.controllers;

import com.gfa.dtos.responsedtos.helloMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/hello")
    public ResponseEntity<?> index() {
        return ResponseEntity.ok(new helloMessageDTO("Hello world!"));
    }
}
