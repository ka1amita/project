package com.gfa.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.gfa.filters.CustomAuthenticationFilter;
import com.gfa.filters.CustomAuthorizationFilter;
import com.gfa.repositories.AppUserRepository;
import com.gfa.services.TokenService;
import com.gfa.utils.Endpoint;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {
    //add
    private final AppUserRepository appUserRepository;

    private final MessageSource messageSource;

    public SecurityConfig(AppUserRepository appUserRepository, MessageSource messageSource) {
        this.appUserRepository = appUserRepository;
        this.messageSource = messageSource;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailsService userDetailsService)
            throws Exception {

        AuthenticationManager authenticationManager =
                http.getSharedObject(AuthenticationManagerBuilder.class)
                        .userDetailsService(userDetailsService)
                        .passwordEncoder(bCryptPasswordEncoder)
                        .and()
                        .build();
        http.authenticationManager(authenticationManager);
        return authenticationManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, TokenService tokenService) throws Exception {
        http.csrf()
                .disable();
        http.sessionManagement()
                .sessionCreationPolicy(STATELESS);
        http.authorizeRequests()
                .antMatchers(Endpoint.HELLO_WORLD.getValue())
                .permitAll();
        http.authorizeRequests()
                .antMatchers(GET,
                        Endpoint.VERIFY_EMAIL_WITH_TOKEN.getValue() + "/*",
                        Endpoint.CONFIRM_WITH_CODE.getValue() + "/*")
                .permitAll();
        http.authorizeRequests()
                .antMatchers(POST,
                        Endpoint.REGISTER.getValue(),
                        Endpoint.LOGIN.getValue(),
                        Endpoint.REFRESH_TOKEN.getValue(),
                        Endpoint.RESET_PASSWORD.getValue(),
                        Endpoint.RESET_PASSWORD.getValue() + "/*",
                        Endpoint.RESEND_VERIFICATION_EMAIL.getValue())
                .permitAll();
        http.authorizeRequests()
                .antMatchers(Endpoint.STRINGS.getValue())
                .permitAll();
        http.authorizeRequests()
                .anyRequest()
                .authenticated(); // the rest requires some Role
        http.addFilter(new CustomAuthenticationFilter(authenticationManager, tokenService, messageSource));
        http.addFilterBefore(new CustomAuthorizationFilter(tokenService, appUserRepository),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}