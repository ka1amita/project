package com.gfa.security;

import com.gfa.filters.CustomAuthenticationFilter;
import com.gfa.filters.CustomAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
public class NewSecurityConfig {

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
    return authenticationManager;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http.csrf()
        .disable();
    http.sessionManagement()
        .sessionCreationPolicy(STATELESS);
    http.authorizeRequests()
        .antMatchers("/login", "/token/refresh", "/hello", "/user/activate")
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
        .authenticated(); // the rest requires any Role
    http.addFilter(new CustomAuthenticationFilter(authenticationManager));
    http.addFilterBefore(new CustomAuthorizationFilter(),
                         UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}