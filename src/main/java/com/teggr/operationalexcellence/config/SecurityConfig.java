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
                .requestMatchers("/", "/new", "/*/edit", "/h2-console/**").permitAll()
                .requestMatchers("/github/repositories").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/github/login")
                .defaultSuccessUrl("/github/repositories", true)
            )
            .logout(logout -> logout
                .logoutUrl("/github/logout")
                .logoutSuccessUrl("/")
            );
        
        return http.build();
    }
}
