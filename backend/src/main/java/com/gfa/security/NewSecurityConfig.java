package com.gfa.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.gfa.filters.CustomAuthenticationFilter;
import com.gfa.filters.CustomAuthorizationFilter;
import com.gfa.services.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class NewSecurityConfig {

  public static final String REGISTER = "/register";
  public static final String LOGIN = "/login";
  public static final String RESET_PASSWORD = "/reset-password";
  public static final String RESET_PASSWORD_WITH_TOKEN = "/reset-password/*";
  public static final String VERIFY_EMAIL_WITH_TOKEN = "/email/verify/*";
  public static final String RESEND_VERIFICATION_EMAIL = "/email/verify/resend";
  public static final String DASHBOARD = "/dashboard";

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http,
                                                     PasswordEncoder passwordEncoder,
                                                     UserDetailsService userDetailsService)
      throws Exception {

    AuthenticationManager authenticationManager =
        http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
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
        .antMatchers(POST,
                     REGISTER,
                     LOGIN,
                     RESET_PASSWORD,
                     RESET_PASSWORD_WITH_TOKEN,
                     RESEND_VERIFICATION_EMAIL)
        .permitAll();
    http.authorizeRequests()
        .antMatchers(GET, VERIFY_EMAIL_WITH_TOKEN)
        .permitAll(); // or anonymous() ??
    http.authorizeRequests()
        .antMatchers(GET, DASHBOARD)
        .hasAuthority("ROLE_ADMIN");
    http.authorizeRequests()
        .anyRequest()
        .authenticated(); // the rest requires some Role
    http.addFilter(new CustomAuthenticationFilter(tokenService, authenticationManager));
    http.addFilterBefore(new CustomAuthorizationFilter(tokenService),
                         UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}