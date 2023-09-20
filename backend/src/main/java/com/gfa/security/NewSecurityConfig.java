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

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
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
        .antMatchers("/login", "/token/refresh", "/hello", "/register", "/confirm/*")
        .permitAll(); // or anonymous() ??
    http.authorizeRequests()
        .antMatchers(GET, "/user/users/**")
        .hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN");
    http.authorizeRequests()
        .antMatchers(GET, "/dashboard")
        .hasAuthority("ROLE_ADMIN");
    http.authorizeRequests()
        .antMatchers(POST, "/reset")
        .permitAll();
    http.authorizeRequests()
        .antMatchers(POST, "/reset/*")
        .permitAll();
    http.authorizeRequests()
        .anyRequest()
        .authenticated(); // the rest requires some Role
    http.addFilter(new CustomAuthenticationFilter(tokenService, authenticationManager));
    http.addFilterBefore(new CustomAuthorizationFilter(tokenService),
                         UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}