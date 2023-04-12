package com.example.multiauthentication.security.config;

import com.example.multiauthentication.security.common.FormAuthenticationDetailsSource;
import com.example.multiauthentication.security.provider.admin.AdminAuthenticationProvider;
import com.example.multiauthentication.security.provider.user.UserAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
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

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    static class AdminSecurityConfig {

        private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;
        private final AuthenticationSuccessHandler adminAuthenticationSuccessHandler;
        private final AuthenticationFailureHandler adminAuthenticationFailureHandler;

        public AdminSecurityConfig(FormAuthenticationDetailsSource formAuthenticationDetailsSource, AuthenticationSuccessHandler adminAuthenticationSuccessHandler, AuthenticationFailureHandler adminAuthenticationFailureHandler) {
            this.formAuthenticationDetailsSource = formAuthenticationDetailsSource;
            this.adminAuthenticationSuccessHandler = adminAuthenticationSuccessHandler;
            this.adminAuthenticationFailureHandler = adminAuthenticationFailureHandler;
        }

        @Bean
        SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf().disable()
                    .securityMatcher("/admin/**")
                    .authorizeHttpRequests((authz) -> {
                        authz.requestMatchers("/admin/sign/**").permitAll();
                        authz.anyRequest().hasRole("ADMIN");
                    })
                    .formLogin((formLogin) -> {
                        formLogin.loginPage("/admin/sign/in");
                        formLogin.loginProcessingUrl("/admin/sign/in").permitAll();
                        formLogin.authenticationDetailsSource(formAuthenticationDetailsSource);
                        formLogin.successHandler(adminAuthenticationSuccessHandler);
                        formLogin.failureHandler(adminAuthenticationFailureHandler);
                    })
                    .authenticationProvider(adminAuthenticationProvider())
                    .build();
        }


        @Bean
        public UserDetailsService adminUserDetailsService() {
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User.withUsername("admin")
                    .password(bCryptPasswordEncoder().encode("adminPass"))
                    .roles("USER", "ADMIN")
                    .build());
            return manager;
        }

        @Bean
        public AuthenticationProvider adminAuthenticationProvider() {
            return new AdminAuthenticationProvider(adminUserDetailsService(), bCryptPasswordEncoder());
        }

    }

    @Configuration
    @Order(2)
    static class UserSecurityConfig {

        private final AuthenticationSuccessHandler userAuthenticationSuccessHandler;
        private final AuthenticationFailureHandler userAuthenticationFailureHandler;

        UserSecurityConfig(AuthenticationSuccessHandler userAuthenticationSuccessHandler, AuthenticationFailureHandler userAuthenticationFailureHandler) {
            this.userAuthenticationSuccessHandler = userAuthenticationSuccessHandler;
            this.userAuthenticationFailureHandler = userAuthenticationFailureHandler;
        }

        @Bean
        SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf().disable()
                    .securityMatcher("/user/**")
                    .authorizeHttpRequests((authz) -> {
                        authz.requestMatchers("/user/sign/**").permitAll();
                        authz.anyRequest().hasRole("USER");
                    })
                    .formLogin((formLogin) -> {
                        formLogin.loginPage("/user/sign/in");
                        formLogin.loginProcessingUrl("/user/sign/in");
                        formLogin.successHandler(userAuthenticationSuccessHandler);
                        formLogin.failureHandler(userAuthenticationFailureHandler);
                    })
                    .authenticationProvider(userAuthenticationProvider())
                    .build();
        }

        @Bean
        public UserDetailsService userUserDetailsService() {
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User.withUsername("user")
                    .password(bCryptPasswordEncoder().encode("userPass"))
                    .roles("USER")
                    .build());
            return manager;
        }

        @Bean
        public AuthenticationProvider userAuthenticationProvider() {
            return new UserAuthenticationProvider(userUserDetailsService(), bCryptPasswordEncoder());
        }
    }
}
