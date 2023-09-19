package com.gfa.controllers;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.dtos.responsedtos.ResponseTokenDTO;
import com.gfa.services.AppUserService;
import com.gfa.services.TokenService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

  private final AppUserService appUserService;
  private final TokenService tokenService;

  @Autowired
  public TokenController(AppUserService appUserService, TokenService tokenService) {
    this.appUserService = appUserService;
    this.tokenService = tokenService;
  }


  @GetMapping("/refresh")
  public void refreshTokens(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
    Set<ResponseTokenDTO> tokens = null;
    try {
      tokens = tokenService.refreshTokens(tokenService.parse(request));
    } catch (Exception e) {
        response.setHeader("error", e.getMessage());
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> error = new HashMap<>();
        error.put("error_message", e.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }

    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    HttpServletResponse response1 = response;
  }


}
