// package com.gfa.controllers;
//
// import com.gfa.dtos.requestdtos.LoginRequestDTO;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// public class LoginController {
//
//     private final AuthenticationManager authenticationManager;
//
//     @Autowired
//     public LoginController(AuthenticationManager authenticationManager) {
//         this.authenticationManager = authenticationManager;
//     }
//
//     @PostMapping("/login")
//     public ResponseEntity<String> login(LoginRequestDTO loginRequestDto) {
//         Authentication authentication = authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginInput(),loginRequestDto.getPassword()));
//         SecurityContextHolder.getContext().setAuthentication(authentication);
//         return new ResponseEntity<>("User login success", HttpStatus.OK);
//     }
// }
