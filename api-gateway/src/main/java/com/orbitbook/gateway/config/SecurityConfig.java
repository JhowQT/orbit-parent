package com.orbitbook.gateway.config;

import com.orbitbook.gateway.security.JwtGatewayFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtGatewayFilter jwtGatewayFilter,
            CorsConfigurationSource corsConfigurationSource)
            throws Exception {

        http
                .cors(cors -> cors
                        .configurationSource(
                                corsConfigurationSource
                        )
                )

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                .addFilterBefore(
                        jwtGatewayFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
