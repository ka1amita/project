package com.gfa.config;

import com.gfa.utils.Endpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(Endpoint.LOGIN, corsConfiguration);
        source.registerCorsConfiguration(Endpoint.REGISTER, corsConfiguration);
        source.registerCorsConfiguration(Endpoint.DASHBOARD, corsConfiguration);
        source.registerCorsConfiguration(Endpoint.RESET_PASSWORD, corsConfiguration);
        source.registerCorsConfiguration(Endpoint.RESET_PASSWORD + "/*", corsConfiguration);
        source.registerCorsConfiguration(Endpoint.TODO_API, corsConfiguration);
        source.registerCorsConfiguration(Endpoint.TODO_API + "/*", corsConfiguration);
        return source;
    }
}