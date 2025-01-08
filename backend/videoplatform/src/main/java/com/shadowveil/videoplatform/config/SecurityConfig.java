package com.shadowveil.videoplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for Swagger and development purposes
                .authorizeHttpRequests(auth -> auth
                        // Allow unrestricted access to Swagger UI and API docs
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Allow unrestricted access to the H2 Console
                        .requestMatchers("/h2-console/**").permitAll()
                        // Secure all other endpoints
                        .anyRequest().authenticated()
                )
                // Allow H2 Console frames in browsers
                .headers(headers -> headers.frameOptions().sameOrigin());

        return http.build();
    }
}
