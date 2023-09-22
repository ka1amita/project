package com.gfa.filters;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.dtos.responsedtos.ErrorResponseDTO;
import com.gfa.services.TokenService;
import com.gfa.utils.Endpoint;
import java.io.IOException;
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

  private static void handleException(HttpServletResponse response, Exception e)
      throws IOException {
    String message = e.getMessage();
    response.setHeader("Error", message);
    response.setStatus(UNAUTHORIZED.value());
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(),
                                  new ErrorResponseDTO(message));
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException,
                                                                            IOException {
    if (request.getServletPath()
               .equals(Endpoint.HELLO_WORLD.getValue()) ||
        request.getServletPath()
               .equals(Endpoint.REGISTER.getValue()) ||
        request.getServletPath()
               .equals(Endpoint.LOGIN.getValue()) ||
        request.getServletPath()
               .equals(Endpoint.REFRESH_TOKEN.getValue()) ||
        request.getServletPath()
               .startsWith(Endpoint.RESET_PASSWORD.getValue()) ||
        request.getServletPath()
               .equals(Endpoint.RESEND_VERIFICATION_EMAIL.getValue()) ||
        request.getServletPath()
               .startsWith(Endpoint.VERIFY_EMAIL_WITH_TOKEN.getValue()) ||
        request.getServletPath()
               .startsWith(Endpoint.CONFIRM_WITH_CODE.getValue())
    ) {

      filterChain.doFilter(request, response);
    } else {
      try {
        Authentication authentication =
            tokenService.getAuthentication(tokenService.mapToDto(request));

        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);

      } catch (Exception e) {
        handleException(response, e);
      } finally {
        filterChain.doFilter(request, response);
      }
    }
  }
}
