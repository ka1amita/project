package com.matejkala.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.matejkala.filters.CustomAuthenticationFilter;
import com.matejkala.filters.CustomAuthorizationFilter;
import com.matejkala.filters.RibbonFilter;
import com.matejkala.utils.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {
    private final RibbonFilter ribbonFilter;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(RibbonFilter ribbonFilter,
                          CustomAuthorizationFilter customAuthorizationFilter,
                          UserDetailsService userDetailsService,
                          CustomAuthenticationFilter customAuthenticationFilter) {
        this.ribbonFilter = ribbonFilter;
        this.customAuthorizationFilter = customAuthorizationFilter;
        this.customAuthenticationFilter = customAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {

        AuthenticationManager authenticationManager =
                http.getSharedObject(AuthenticationManagerBuilder.class)
                        .userDetailsService(userDetailsService)
                        .passwordEncoder(passwordEncoder())
                        .and()
                        .build();
        http.authenticationManager(authenticationManager);
        return authenticationManager;
    }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http
                                        ) throws Exception {
    http.csrf()
        .disable();
    http.cors();
    http.sessionManagement()
        .sessionCreationPolicy(STATELESS);
    http.authorizeRequests()
        .antMatchers(GET,
                     Endpoint.VERIFY_EMAIL_WITH_TOKEN +"/*",
                     Endpoint.CONFIRM_WITH_CODE + "/*",
                     Endpoint.RIBBON)
        .permitAll();
    http.authorizeRequests()
        .antMatchers(POST,
                     Endpoint.REGISTER,
                     Endpoint.LOGIN,
                     Endpoint.REFRESH_TOKEN,
                     Endpoint.RESET_PASSWORD,
                     Endpoint.RESET_PASSWORD + "/*",
                     Endpoint.RESEND_VERIFICATION_EMAIL)
        .permitAll();
    http.authorizeRequests()
              .antMatchers(Endpoint.STRINGS)
              .permitAll();
    http.authorizeRequests()
        .anyRequest()
        .authenticated(); // the rest require some Role
      http.addFilterBefore(ribbonFilter, UsernamePasswordAuthenticationFilter.class);
      http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
      http.addFilter(customAuthenticationFilter);
    return http.build();
  }
}