package com.gfa.filters;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.services.TokenService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

private TokenService tokenService;

  public CustomAuthorizationFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    if (request.getServletPath()
               .equals("/login") ||
        request.getServletPath()
               .equals("/token/refresh") ||
        request.getServletPath()
               .equals("/user/activate") ||
        request.getServletPath()
               .equals("/hello") ||
        request.getServletPath()
               .equals("/api/user/activate") ||
        request.getServletPath()
               .equals("/reset") ||
        request.getServletPath()
               .equals("/reset/*")) {
      filterChain.doFilter(request, response);
    } else {
      String authorizationHeader = request.getHeader(AUTHORIZATION);

      String prefix = "Bearer ";
      if (authorizationHeader != null && authorizationHeader.startsWith(prefix)) {
        try {
          String token = authorizationHeader.substring(prefix.length());

          SecurityContextHolder.getContext()
                               .setAuthentication(tokenService.getAuthenticationToken(token));

          filterChain.doFilter(request, response);
        } catch (Exception e) {
          response.setHeader("error", e.getMessage());
          response.setStatus(FORBIDDEN.value());
          response.setContentType(APPLICATION_JSON_VALUE);
          Map<String, String> error = new HashMap<>();
          error.put("error_message", e.getMessage());

          new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
      } else {
        filterChain.doFilter(request, response);
      }
    }
  }
}
