package com.gfa.controllers;

import static com.gfa.utils.Endpoint.REGISTER;

import com.gfa.dtos.responsedtos.HelloMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @RequestMapping(REGISTER)
    public ResponseEntity<?> index() {
        return ResponseEntity.ok(new HelloMessageDTO("Hello world!"));
    }
}
