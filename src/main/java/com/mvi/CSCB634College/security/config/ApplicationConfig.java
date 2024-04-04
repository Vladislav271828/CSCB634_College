package com.mvi.CSCB634College.security.config;


import com.mvi.CSCB634College.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures components necessary for Spring Security in the application.
 * This configuration includes the user details service, authentication provider,
 * password encoder, and authentication manager.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    /**
     * Defines the UserDetailsService that loads user-specific data.
     * It uses the UserRepository to load a user by username (email in this context).
     *
     * @return A UserDetailsService that loads user information.
     * @throws UsernameNotFoundException if the user cannot be found by the provided username (email).
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Configures the DaoAuthenticationProvider with the custom UserDetailsService and
     * the password encoder. This authentication provider is responsible for authenticating
     * a user with the database.
     *
     * @return An AuthenticationProvider that uses a data access object (DAO) to authenticate users.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the password encoder to be used for encoding passwords in the application.
     * This implementation uses BCryptPasswordEncoder.
     *
     * @return A PasswordEncoder that uses the BCrypt hashing function.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager from AuthenticationConfiguration to be used
     * throughout the application. The AuthenticationManager is a crucial component of
     * Spring Security that manages authentication operations.
     *
     * @param config The AuthenticationConfiguration provided by Spring Security.
     * @return An AuthenticationManager bean to manage authentication.
     * @throws Exception if there's an error creating the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
