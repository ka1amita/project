package com.gfa.filters;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.dtos.responsedtos.ErrorResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.repositories.AppUserRepository;
import com.gfa.services.TokenService;
import com.gfa.utils.Endpoint;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    //add
    private final AppUserRepository appUserRepository;


    @Autowired
    public CustomAuthorizationFilter(TokenService tokenService, AppUserRepository appUserRepository) {
        this.tokenService = tokenService;
        this.appUserRepository = appUserRepository;
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
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
            IOException {
        System.err.println("inside filter");
        if (request.getServletPath().startsWith(Endpoint.CONFIRM_WITH_CODE.getValue())) {
            System.err.println("insideactivateendpoint");
            String[] subPath = request.getServletPath().split("/");
            String activationCode = subPath[subPath.length - 1];
            Optional<AppUser> userWithCode = appUserRepository.findAppUserByActivationCode(activationCode);
            if (userWithCode.isPresent()) {
                setPreferredLanguageForAppUser(userWithCode);
            }
        }
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
                        .startsWith(Endpoint.CONFIRM_WITH_CODE.getValue())||
                request.getServletPath()
                .startsWith(Endpoint.STRINGS.getValue())
        ) {
            filterChain.doFilter(request, response);
        } else {
            try {
                Authentication authentication =
                        tokenService.getAuthentication(tokenService.mapToDto(request));
//---------------
                if (authentication != null) {
                    System.err.println("auth is not null");
                    AppUser user = (AppUser) authentication.getPrincipal();
                    setPreferredLanguage(user.getUsername());
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
//---------------
            } catch (Exception e) {
                handleException(response, e);
            } finally {
                filterChain.doFilter(request, response);
            }
        }
    }

    private void setPreferredLanguage(String username) {
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            String preferredLang = appUser.get().getPreferredLanguage();
            System.err.println("setting preffered language");
            LocaleContextHolder.setLocale(new Locale(preferredLang));
        }
    }

    private void setPreferredLanguageForAppUser(Optional<AppUser> appUser) {
        if (appUser.isPresent()) {
            String preferredLang = appUser.get().getPreferredLanguage();
            System.err.println("setting preffered language2");
            System.err.println(preferredLang);
            LocaleContextHolder.setLocale(new Locale(preferredLang));
        }
    }
}