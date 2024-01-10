package com.matejkala.controllers;

import static com.matejkala.utils.Endpoint.REFRESH_TOKEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matejkala.dtos.responsedtos.ResponseTokensDTO;
import com.matejkala.services.TokenService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

  private final TokenService tokenService;

  @Autowired
  public TokenController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping(REFRESH_TOKEN)
  public void refreshTokens(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
    ResponseTokensDTO tokens = tokenService.refreshTokens(tokenService.mapToDto(request));

    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
}
