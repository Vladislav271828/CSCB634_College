package com.mvi.CSCB634College.security.config;


import com.mvi.CSCB634College.exception.UserNotFound;
import com.mvi.CSCB634College.security.token.TokenRepository;
import com.mvi.CSCB634College.user.User;
import com.mvi.CSCB634College.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling logout requests within the Spring Security framework.
 * It intercepts logout requests, extracts the JWT from the Authorization header, and
 * marks the associated tokens as expired and revoked in the database.
 * This custom logout handler is designed to integrate with Spring Security's logout mechanism
 * to provide additional processing related to JWT invalidation upon user logout.
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    /**
     * Handles the logout logic by invalidating the JWT associated with the current user session.
     * This method is automatically invoked by Spring Security during the logout process.
     * It extracts the JWT from the "Authorization" header, verifies its existence and validity,
     * and then proceeds to mark the token and any other valid tokens associated with the user as
     * expired and revoked in the repository.
     *
     * @param request        the HTTP request object containing the "Authorization" header with the JWT
     * @param response       the HTTP response object, not directly used but required by the interface
     * @param authentication the current authentication object, not directly used but required by the interface
     */
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        User user;
        try {

            user = userRepository.findUserByToken(jwt).orElseThrow();
        } catch (Exception e) {

            throw new UserNotFound(e.getMessage());
        }


        if (storedToken != null) {
            var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
            if (validUserTokens.isEmpty()) {
                return;
            }
            validUserTokens.forEach(t -> {
                t.setExpired(true);
                t.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
}