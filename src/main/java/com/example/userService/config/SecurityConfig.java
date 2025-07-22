package com.example.userService.config;

import com.example.userService.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration class.
 * <p>
 * This class sets up the application's security rules, password encoding,
 * and integrates a custom JWT-based authentication filter.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /** Custom filter that handles JWT token validation. */
    private final JwtFilter jwtFilter;


    /**
     * Configures the HTTP security for the application.
     * <ul>
     *     <li>Disables CSRF for stateless APIs.</li>
     *     <li>Allows unauthenticated access to the {@code /user/sign-up} endpoint.</li>
     *     <li>Requires authentication for all other endpoints.</li>
     *     <li>Adds a custom JWT filter before the standard username-password filter.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/sign-up").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}