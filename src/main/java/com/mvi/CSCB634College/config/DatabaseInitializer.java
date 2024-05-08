package com.mvi.CSCB634College.config;

import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.auth.AuthenticationService;
import com.mvi.CSCB634College.security.auth.RegisterRequest;
import com.mvi.CSCB634College.user.User;
import com.mvi.CSCB634College.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public DatabaseInitializer(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findAll().isEmpty()) {

            RegisterRequest registerRequest = RegisterRequest.builder().firstName("admin").lastName("adminovich").email("admin@gmail.com").password("STRONGPASSWORD!@#").build();

            authenticationService.register(registerRequest);
            User user = userRepository.findByEmail("admin@gmail.com").orElseThrow();
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }
}