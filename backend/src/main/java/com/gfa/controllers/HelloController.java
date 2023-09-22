package com.gfa.controllers;

import static com.gfa.utils.Endpoint.HELLO_WORLD;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @RequestMapping(HELLO_WORLD)
    public ResponseEntity<?> index() {
        return ResponseEntity.ok("Hello world!");
    }
}
