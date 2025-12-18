package com.teggr.operationalexcellence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
            )
            // CSRF is disabled as this is a single-user local application
            // without session-based authentication. All requests are permitted.
            // For production use, consider implementing proper CSRF protection.
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
