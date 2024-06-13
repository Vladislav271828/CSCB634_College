package com.mvi.CSCB634College.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configures the security settings for the application, defining how CORS, CSRF, session management,
 * authentication, and authorization are handled. It sets up a security filter chain that includes
 * custom authentication and logout handling, as well as configuring CORS to allow requests from
 * specific origins.
 * <p>
 * This configuration is crucial for applying security measures to protect the application against
 * common vulnerabilities and ensuring that only authenticated and authorized users can access
 * certain resources.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    /**
     * Defines the security filter chain that applies to HTTP requests, configuring security
     * behaviors such as CORS, CSRF prevention, session management, and routes security.
     * It integrates custom authentication and logout handling into the Spring Security filter chain.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during the configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/user/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/grade/professor/**").hasAuthority("PROFESSOR")
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/ws-message/**", "/ws/**", "/ws-endpoint/**").permitAll()  // Allow the WebSocket endpoints
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext())
                );
        return http.build();
    }


    /**
     * Configures the CORS policy for the application, allowing requests from specific origins
     * with certain HTTP methods, headers, and credentials. This bean is essential for cross-origin
     * resource sharing, enabling the frontend application to communicate with the backend securely.
     *
     * @return the {@link CorsConfigurationSource} that provides the CORS configuration
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}