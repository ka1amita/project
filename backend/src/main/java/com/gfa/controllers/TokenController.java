package com.gfa.controllers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.services.AppUserService;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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

  @Autowired
  public TokenController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @GetMapping("/refresh")
  public void refreshToken(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
    String authorizationHeader = request.getHeader(AUTHORIZATION);

    String prefix = "Bearer ";
    if (authorizationHeader != null && authorizationHeader.startsWith(prefix)) {
      try {
        String refresh_token = authorizationHeader.substring(prefix.length());
        // TODO Refactor the algorithm to some class in utility but must match the secret in the
        //  other Filter
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                                  .build();
        DecodedJWT decodedJwt = verifier.verify(refresh_token);

        String username = decodedJwt.getSubject();

        AppUser appUser = appUserService.getAppUser(username);

        String access_token = JWT.create()
                                 .withSubject(appUser.getUsername())
                                 .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 3600000))
                                 .withIssuer(request.getRequestURL()
                                                    .toString())
                                 .withClaim("roles", appUser.getRoles()
                                                            .stream()
                                                            .map(Role::getName)
                                                            .collect(
                                                                Collectors.toList()))
                                 .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
      } catch (Exception e) {
        response.setHeader("error", e.getMessage());
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> error = new HashMap<>();
        error.put("error_message", e.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }
    } else {
      throw new RuntimeException("Refresh token is missing");
    }
  }
}
