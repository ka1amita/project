package com.matejkala.controllers;

import com.matejkala.dtos.responsedtos.ResponseDTO;
import static com.matejkala.utils.Endpoint.DASHBOARD;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DASHBOARD)
public class DashboardController {
    @GetMapping({"", "/"})
    public ResponseEntity<? extends ResponseDTO> index() {
        return ResponseEntity.ok().build();
    }
}
