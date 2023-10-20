package com.gfa.filters;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    public TestController() {
    }

    @GetMapping()
    public ResponseEntity<?> testEnvironmentHeader(@RequestAttribute HttpServletRequest response) {
        return ResponseEntity.ok(null);
    }
}
