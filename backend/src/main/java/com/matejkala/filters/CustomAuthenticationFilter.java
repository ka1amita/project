package com.matejkala.filters;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matejkala.dtos.responsedtos.ResponseTokensDTO;
import com.matejkala.services.TokenService;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenService tokenService;
    private final MessageSource messageSource;

    @Autowired
    public CustomAuthenticationFilter(TokenService tokenService,
                                      @Lazy AuthenticationManager authenticationManager,
                                      MessageSource messageSource) {
        this.tokenService = tokenService;
        this.setAuthenticationManager(authenticationManager);
        this.messageSource = messageSource;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        Locale currentLocale = LocaleContextHolder.getLocale();

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    messageSource.getMessage("error.authentication.method.not.supported", null, currentLocale) + request.getMethod());
        }

        String loginInput, password;

        try {
            Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            if (requestMap.get("loginInput") == null || requestMap.get("loginInput").isEmpty()) {
                throw new BadCredentialsException(messageSource.getMessage("error.bad.credentials.for.username.and.email", null, currentLocale));
            }
            if (requestMap.get("password") == null || requestMap.get("password").isEmpty()) {
                throw new BadCredentialsException(messageSource.getMessage("error.bad.credentials.for.password", null, currentLocale));
            }
            loginInput = requestMap.get("loginInput");
            password = requestMap.get("password");
        } catch (IOException e) {
            throw new AuthenticationServiceException(messageSource.getMessage("error.bad.login.credentials", null, currentLocale));
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginInput, password);
        return this.getAuthenticationManager()
                   .authenticate(authenticationToken);
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
        Locale currentLocale = LocaleContextHolder.getLocale();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error",messageSource.getMessage("error.unauthorized", null, currentLocale));
        errorDetails.put("message", messageSource.getMessage("error.bad.credentials", null, currentLocale));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), errorDetails);
    }
}
