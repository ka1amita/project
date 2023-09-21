package com.gfa.filters;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.dtos.responsedtos.ResponseTokensDTO;
import com.gfa.services.TokenService;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;


  public CustomAuthenticationFilter(TokenService tokenService,
                                    AuthenticationManager authenticationManager) {
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
  }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
      if (!request.getMethod().equals("POST")) {
        throw new AuthenticationServiceException(
            "Authentication method not supported: " + request.getMethod());
      }

      String username = obtainUsername(request);
      username = (username != null) ? username.trim() : "";
      String password = obtainPassword(request);
      password = (password != null) ? password : "";

      if (username.isEmpty()) {
        throw new BadCredentialsException("Please provide a username or an email.");
      }
      if (password.isEmpty()) {
        throw new BadCredentialsException("Please provide a password.");
      }

      UsernamePasswordAuthenticationToken
          authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                                                                            password);
      setDetails(request, authRequest);
      return authenticationManager.authenticate(authRequest);
    }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authentication)
      throws IOException, ServletException {

    User user = (User) authentication.getPrincipal();
    String username = user.getUsername();
    Collection<GrantedAuthority> authorities = user.getAuthorities();
    String issuer = request.getRequestURL()
                           .toString();
    ResponseTokensDTO tokens = tokenService.createTokens(username, issuer, authorities);

    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getWriter(), tokens);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException exception)
      throws IOException, ServletException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("error", "Unauthorized");
    errorDetails.put("message", exception.getMessage());
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getWriter(), errorDetails);
  }
}
