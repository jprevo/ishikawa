package org.sitsgo.ishikawa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminUserService adminUserService;

    private final AuthenticationConfiguration configuration;

    public SecurityConfig(AdminUserService adminUserService, AuthenticationConfiguration configuration) {
        this.adminUserService = adminUserService;
        this.configuration = configuration;
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Autowired
    void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(adminUserService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] authEndPoints = {"/login", "/logout", "/auth", "/api/user/me"};
        String[] frontendEndPoints = {"/", "/index.html", "/assets/**"};

        return http
                .authorizeHttpRequests(authCustomizer -> authCustomizer
                        .requestMatchers(authEndPoints).permitAll()
                        .requestMatchers(frontendEndPoints).permitAll()
                        .anyRequest().authenticated()
                ).build();
    }
}