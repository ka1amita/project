package com.gfa.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.gfa.filters.CustomAuthenticationFilter;
import com.gfa.filters.CustomAuthorizationFilter;
import com.gfa.filters.RibbonFilter;
import com.gfa.services.EnvironmentService;
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
    private final MessageSource messageSource;

    public SecurityConfig(MessageSource messageSource) {
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
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, TokenService tokenService,
                                         RibbonProperties ribbonProperties,
                                         EnvironmentService environmentService) throws Exception {
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
    http.addFilterBefore(new RibbonFilter(ribbonProperties, environmentService), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(new CustomAuthorizationFilter(tokenService),
                           UsernamePasswordAuthenticationFilter.class);
    http.addFilter(
        new CustomAuthenticationFilter(authenticationManager, tokenService, messageSource));
    return http.build();
  }
}