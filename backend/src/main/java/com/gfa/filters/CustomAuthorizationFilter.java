package com.gfa.filters;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.exceptions.MissingBearerTokenException;
import com.gfa.services.TokenService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

  private final TokenService tokenService;

  @Autowired
  public CustomAuthorizationFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException,
                                                                            IOException {
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
               .startsWith("/reset/")) {

      filterChain.doFilter(request, response);
    } else {
      // try {
        Authentication authenticationToken =
            tokenService.getAuthenticationToken(tokenService.mapToDto(request));
        SecurityContextHolder.getContext()
                             .setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);

      // } catch (MissingBearerTokenException ok) {
      //   filterChain.doFilter(request, response);
      // } catch (Exception e) {
      //   //     TODO ask Lan about the Exception
      //   response.setHeader("error", e.getMessage());
      //   response.setStatus(FORBIDDEN.value());
      //   Map<String, String> error = new HashMap<>();
      //   error.put("error_message", e.getMessage());
      //   response.setContentType(APPLICATION_JSON_VALUE);
      //   new ObjectMapper().writeValue(response.getOutputStream(), error);
      // }
    }
  }
}
