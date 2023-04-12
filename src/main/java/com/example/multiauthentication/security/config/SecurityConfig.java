package com.example.multiauthentication.security.config;

import com.example.multiauthentication.security.common.FormAuthenticationDetailsSource;
import com.example.multiauthentication.security.provider.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    static class AdminSecurityConfig {

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
                    .csrf().disable()
                    .authorizeHttpRequests((authz) -> {
                        authz.requestMatchers("/admin/sign/**").permitAll();
                        authz.requestMatchers("/admin/**").hasRole("ADMIN");
                    })
                    .formLogin((formLogin) -> {
                        formLogin.loginPage("/admin/sign/in");
                        formLogin.loginProcessingUrl("/admin/sign/in").permitAll();
                        formLogin.authenticationDetailsSource(formAuthenticationDetailsSource);
                        formLogin.successHandler(authenticationSuccessHandler);
                        formLogin.failureHandler(authenticationFailureHandler);
                    })
                    .authenticationProvider(authenticationProvider())
                    .build();
        }


        @Bean
        public UserDetailsService userDetailsService() {
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User.withUsername("user")
                    .password(bCryptPasswordEncoder().encode("userPass"))
                    .roles("USER")
                    .build());
            manager.createUser(User.withUsername("admin")
                    .password(bCryptPasswordEncoder().encode("adminPass"))
                    .roles("USER", "ADMIN")
                    .build());
            return manager;
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            return new CustomAuthenticationProvider(userDetailsService(), bCryptPasswordEncoder());
        }

    }

    @Configuration
    @Order(2)
    static class UserSecurityConfig {
        @Bean
        SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
            return http
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
