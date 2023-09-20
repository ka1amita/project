package com.gfa.controllers;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.dtos.responsedtos.ResponseTokensDTO;
import com.gfa.services.TokenService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

  private final TokenService tokenService;

  @Autowired
  public TokenController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @GetMapping("/refresh")
  public void refreshTokens(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
      ResponseTokensDTO tokens = null;
    // try {
        tokens = tokenService.refreshTokens(tokenService.mapToDto(request));
    //     TODO ask Lan about the Exception
    // } catch (Exception e) {
    //     response.setHeader("error", e.getMessage());
    //     response.setStatus(FORBIDDEN.value());
    //     response.setContentType(APPLICATION_JSON_VALUE);
    //     Map<String, String> error = new HashMap<>();
    //     error.put("error_message", e.getMessage());
    //     new ObjectMapper().writeValue(response.getOutputStream(), error);
    //   }

    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
}
