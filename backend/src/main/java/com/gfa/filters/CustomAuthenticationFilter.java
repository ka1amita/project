package com.gfa.filters;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.services.TokenService;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                    TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (request.getParameter("username") == null || request.getParameter("username").isEmpty()) {
            throw new AuthenticationException("Please provide a username or an email.") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }
        if (request.getParameterMap().get("password") == null || request.getParameter("password").isEmpty()) {
            throw new AuthenticationException("Please provide a password.") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authentication)
      throws IOException, ServletException {

    User user = (User) authentication.getPrincipal();
    String username = user.getUsername();
    Collection<GrantedAuthority> authorities = user.getAuthorities();
    Calendar now = Calendar.getInstance();
    String issuer = request.getRequestURL()
                           .toString();

    String access_token = tokenService.getToken(username, now, issuer, authorities);
    String refresh_token = tokenService.getToken(username, now, issuer);

    Map<String, String> tokens = new HashMap<>();
    tokens.put("access_token", access_token);
    tokens.put("refresh_token", refresh_token);
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
