package com.mvi.CSCB634College.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvi.CSCB634College.exception.DatabaseException;
import com.mvi.CSCB634College.exception.ErrorResponse;
import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.token.Token;
import com.mvi.CSCB634College.security.token.TokenRepository;
import com.mvi.CSCB634College.security.token.TokenType;
import com.mvi.CSCB634College.user.User;
import com.mvi.CSCB634College.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Custom filter for JWT authentication. It extends {@link OncePerRequestFilter} to ensure
 * it is executed once per request. This filter intercepts the request to extract and validate
 * the JWT token from the Authorization header, authenticating and setting the security context
 * if the token is valid.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    /**
     * The main method that executes the filter logic. It checks the Authorization header for
     * a JWT token, validates the token, and sets the authentication in the security context.
     *
     * @param request     The request object provided by the servlet container.
     * @param response    The response object provided by the servlet container.
     * @param filterChain The filter chain provided by the Spring security filter chain.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs during the processing of the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        try {
            String userEmail = jwtService.extractUsername(jwt);
            logger.debug("Attempting to authenticate user: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;
                try {

                    userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                } catch (UsernameNotFoundException e) {
                    sendErrorResponse(response, "User Not Found", HttpStatus.NOT_FOUND.value());
                    return;
                }
                var isTokenValid = tokenRepository.findByToken(jwt)
                        .map(t -> !t.isRevoked() && !t.isExpired())
                        .orElse(false);


                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    logger.info("JWT token is valid for user: {}", userEmail);

                    List<Token> allTokens = tokenRepository.findAllValidTokensByUser(userRepository.findUserByToken(jwt).orElseThrow().getId());
                    for (Token token : allTokens) {
                        if (token.getTokenType().equals(TokenType.REFRESH_TOKEN) && token.getExpirationTime().before(new Date())) {
                            revokeTokens(allTokens);
                            sendErrorResponse(response, "Refresh Token Expired. Requires To Log In Again.", HttpStatus.UNAUTHORIZED.value());
                            return;
                        }
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.info("JWT token validation failed for user: {}", userEmail);

                    sendErrorResponse(response, "Expired JWT Access Token.", HttpStatus.UNAUTHORIZED.value());
                    return;
                }

                if (request.getRequestURI().contains("/api/v1/admin")) {
                    logger.info("User has role: {}", userDetails.getAuthorities().toString());


                    //Check if the User has authority ADMIN
                    if (userDetails.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals(Role.ADMIN.toString()))) {
                        sendErrorResponse(response, "User with role " + userDetails.getAuthorities().toString() + " doesn't have permissions to perform the action. Required role " + Role.ADMIN, HttpStatus.UNAUTHORIZED.value());
                        return;
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (MalformedJwtException e) {
            sendErrorResponse(response, e.getMessage(), HttpStatus.BAD_REQUEST.value());

        } catch (ExpiredJwtException ex) {
            logger.error("JWT token expired: {}", ex.getMessage(), ex);

            if (request.getRequestURI().equals("/api/v1/auth/refresh")) {
                Token token;
                try {
                    token = tokenRepository.findByToken(jwt).orElseThrow();
                } catch (NoSuchElementException e) {
                    sendErrorResponse(response, "Token Not found", HttpStatus.NOT_FOUND.value());
                    return;
                }

                boolean isMostRecentAccessToken = isMostRecent(token);
                if (!isMostRecentAccessToken) {
                    sendErrorResponse(response, "JWT Access Token Is Not Most Recent.", HttpStatus.UNAUTHORIZED.value());
                } else {

                    token.setRevoked(true);
                    token.setExpired(true);
                    tokenRepository.save(token);
                    filterChain.doFilter(request, response);
                }
            } else {
                logger.error("Expired JWT token.", ex);
                sendErrorResponse(response, "Expired JWT token.", HttpStatus.UNAUTHORIZED.value());
            }
        } catch (UsernameNotFoundException ex) {
            logger.error("User not found: {}", ex.getMessage(), ex);
            sendErrorResponse(response, "User not found.", HttpStatus.NOT_FOUND.value());
        } catch (DatabaseException dbExc) {
            logger.error("Database Exception caught: {}", dbExc.getMessage(), dbExc);
            sendErrorResponse(response, "User not found.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception e) {
            logger.error("Oops error: {}", e.getMessage(), e);

            sendErrorResponse(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Revokes all tokens provided in the list. This method sets the 'revoked' and 'expired'
     * flags to true for each token and updates the records in the database.
     *
     * @param allTokens The list of tokens to be revoked.
     */
    private void revokeTokens(List<Token> allTokens) {
        for (Token token : allTokens) {
            token.setRevoked(true);
            token.setExpired(true);
        }

        tokenRepository.saveAll(allTokens);
    }

    /**
     * Checks if the provided token is the most recent access token issued for the user.
     * It compares the issue date of the provided token against all access tokens of the user.
     *
     * @param token The token to check.
     * @return true if the token is the most recent; false otherwise.
     */
    private boolean isMostRecent(Token token) {
        User user = token.getUser();

        List<Token> allUserTokens = tokenRepository.findAllTokensByUserId(user.getId());

        // Filter to include only access tokens, then find the one with the latest IssuedAt date
        Token mostRecentToken = allUserTokens.stream()
                .filter(t -> t.getTokenType() == TokenType.ACCESS_TOKEN)
                .max(Comparator.comparing(Token::getIssuedAt))
                .orElse(null); // Use orElse(null) to handle the case where the list might be empty

        // Check if the most recent access token is the same as the token passed as an argument
        return mostRecentToken != null && mostRecentToken.getToken().equals(token.getToken());
    }

    /**
     * Sends a custom error response in JSON format with the provided message and HTTP status.
     * This method is used to communicate errors related to JWT processing back to the client.
     *
     * @param response The HttpServletResponse object to send the error to.
     * @param message  The error message to be included in the response.
     * @param status   The HTTP status code for the error response.
     * @throws IOException If an error occurs while writing to the response object.
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setStatus(status);
        errorResponse.setTimeStamp(System.currentTimeMillis());

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }


}