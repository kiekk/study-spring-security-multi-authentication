package com.example.multiauthentication.security.config;

import com.example.multiauthentication.security.common.FormAuthenticationDetailsSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    class AdminSecurityConfig {

        private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;
        private final AuthenticationSuccessHandler authenticationSuccessHandler;
        private final AuthenticationFailureHandler authenticationFailureHandler;

        public AdminSecurityConfig(FormAuthenticationDetailsSource formAuthenticationDetailsSource, AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler) {
            this.formAuthenticationDetailsSource = formAuthenticationDetailsSource;
            this.authenticationSuccessHandler = authenticationSuccessHandler;
            this.authenticationFailureHandler = authenticationFailureHandler;
        }

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
                    .loginProcessingUrl("/admin/sign/in")
                    .authenticationDetailsSource(formAuthenticationDetailsSource)
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .permitAll()
                    .and()
                    .build();
        }

    }

    @Configuration
    @Order(2)
    class UserSecurityConfig {
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
                    .loginProcessingUrl("/user/sign/in")
                    .permitAll()
                    .and()
                    .build();
        }
    }
}
