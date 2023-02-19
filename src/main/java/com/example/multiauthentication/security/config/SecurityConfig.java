package com.example.multiauthentication.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    static class AdminSecurityConfig {
        @Bean
        SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .securityMatcher("/admin/**")
                    .authorizeHttpRequests(authz -> {
                        authz.requestMatchers("/admin/sign/**").permitAll();
                        authz.requestMatchers("/admin/**").hasRole("ADMIN");
                    })
                    .formLogin()
                    .loginPage("/admin/sign/in")
                    .permitAll()
                    .and()
                    .build();
        }

    }

    @Configuration
    @Order(2)
    static class UserSecurityConfig {
        @Bean
        SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .securityMatcher("/user/**")
                    .authorizeHttpRequests(authz -> {
                        authz.requestMatchers("/user/sign/**").permitAll();
                        authz.requestMatchers("/user/**").hasRole("USER");
                    })
                    .formLogin()
                    .loginPage("/user/sign/in")
                    .permitAll()
                    .and()
                    .build();
        }
    }
}
