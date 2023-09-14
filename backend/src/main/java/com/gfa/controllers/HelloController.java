package com.gfa.controllers;

import com.gfa.dtos.responsedtos.HelloMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<?> index() {
        return ResponseEntity.ok(new HelloMessageDTO("Hello world!"));
    }
}
