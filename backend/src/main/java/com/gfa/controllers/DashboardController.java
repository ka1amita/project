package com.gfa.controllers;

import com.gfa.dtos.responsedtos.ResponseDTO;
import static com.gfa.utils.Endpoint.DASHBOARD;

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
