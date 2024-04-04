package com.mvi.CSCB634College.security.auth;


import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.config.JwtService;
import com.mvi.CSCB634College.security.token.Token;
import com.mvi.CSCB634College.security.token.TokenRepository;
import com.mvi.CSCB634College.security.token.TokenType;
import com.mvi.CSCB634College.user.User;
import com.mvi.CSCB634College.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Provides authentication services for user registration, authentication, and token refreshment.
 * This service handles user credentials, token generation and validation, and user authentication state.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;


    /**
     * Registers a new user with the provided details from the registration request.
     * Encrypts the user's password, assigns a default role, and generates initial JWT access and refresh tokens.
     *
     * @param request The user registration request containing necessary user details.
     * @return An authentication response containing the JWT access token.
     * @throws RuntimeException if a user with the given email already exists.
     */
    public AuthenticationResponse register(RegisterRequest request) {

        //Check if incoming data is valid


        //Check if new user is unique
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists.");
        }

        //create user with request data
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .tokens(new ArrayList<>())
                .build();

        var savedUser = userRepository.save(user); //save user in the db

        var jwtToken = jwtService.generateToken(user); //generate a JWT Token for the user's session (If the user logs out the token will be marked as invalid, if the user authenticates again a new token will be created and the old one will be updated to be invalid)

        saveUserToken(savedUser, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60));//save the token


        // In AuthenticationService's register and authenticate methods

        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 2));//save the token 1209600000


        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }


    /**
     * Authenticates a user with the provided email and password from the authentication request.
     * Generates new JWT access and refresh tokens if authentication is successful.
     *
     * @param request The authentication request containing the user's credentials.
     * @return An authentication response containing the JWT access token.
     * @throws UsernameNotFoundException if no user is found with the provided email.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        //Find a user or throw 404
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();


        //Authenticates the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        //Check if user has active tokens
        List<Token> userTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        String jwtToken = null;

        if (userTokens.isEmpty()) {
            // Revoke all tokens
            revokeAllUserTokens(user);//ensures jwt tokens are set to false
            //Create new Refresh Token
            var refreshToken = jwtService.generateRefreshToken(user);
            //Save RefreshToken
            saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 2));//14 days

            //Create new Access Token
            jwtToken = jwtService.generateToken(user);
            //Save Access Token
            saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60));//1 minute

        } else {
            int counter = 0;
            Token activeRefreshToken = null;

            for (Token token : userTokens) {
                //Revoke all access tokens
                if (token.getTokenType().equals(TokenType.ACCESS_TOKEN)) {
                    token.setExpired(true);
                    token.setRevoked(true);
                }
                //Check if the user has active refresh tokens
                if (token.getTokenType().equals(TokenType.REFRESH_TOKEN)) {
                    counter++;
                }

                if (counter == 1 && token.getTokenType().equals(TokenType.REFRESH_TOKEN)) {
                    activeRefreshToken = token;
                } else if (counter > 1 || counter < 0) {
                    activeRefreshToken = null;
                }
            }

            //If the User has more than one refresh token which is not expired - SECURITY RISK - REVOKE ALL
            if (counter > 1) {
                // Revoke all tokens
                revokeAllUserTokens(user);//ensures jwt tokens are set to false

                //Create new Access Token
                jwtToken = jwtService.generateToken(user);
                //Save Access Token
                saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60));//1 minute

                //Create new Refresh Token
                var refreshToken = jwtService.generateRefreshToken(user);
                //Save RefreshToken
                saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 2));//14 days


            } else if (counter == 1) { //If there is one active refresh token then we check if it's active
                if (activeRefreshToken.getExpirationTime().after(new Date())) {

                    // If the user has only one refresh token, and it's active then create a new access token
                    //Create new Access Token
                    jwtToken = jwtService.generateToken(user);
                    //Save Access Token
                    saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60));//1 minute


                } else {
                    // Revoke all tokens
                    revokeAllUserTokens(user);//ensures jwt tokens are set to false
                    //Create new Refresh Token
                    var refreshToken = jwtService.generateRefreshToken(user);
                    //Save RefreshToken
                    saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 2));//14 days

                    //Create new Access Token
                    jwtToken = jwtService.generateToken(user);
                    //Save Access Token
                    saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60));//1 minute


                }
            }
        }


        // Return Access Token
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }


    /**
     * Refreshes the user's access token using the provided refresh token in the authorization header.
     * Validates the refresh token and generates a new access token if the refresh token is valid.
     *
     * @param authorizationHeader The authorization header containing the bearer access token.
     * @return An authentication response containing the new JWT access token.
     * @throws BadRequestException if the access token is missing, invalid, or not yet expired.
     */
    public AuthenticationResponse refresh(String authorizationHeader) throws BadRequestException {
        //Obtain the token string from the request's header
        String tokenStr;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            tokenStr = authorizationHeader.substring(7); // Remove "Bearer " prefix
        } else {
            throw new BadRequestException("Missing JWT token.");
        }

        //Get the Token object
        Token tokenObj = tokenRepository.findByToken(tokenStr).orElseThrow();

        //Get the User via the Token
        User user = null;
        if (tokenObj.isExpired() && tokenObj.getTokenType().equals(TokenType.ACCESS_TOKEN)) {
            user = userRepository.findUserByToken(tokenObj.getToken()).orElseThrow();
        } else {
            throw new BadRequestException("Can't refresh access token until it expires.");
        }

        // Get all active tokens regardless of their type
        List<Token> activeRefreshTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        // Check all active refresh tokens - we are looking for just one to authorize the creation of a new access token
        boolean hasActiveRefreshToken = false;
        if (!activeRefreshTokens.isEmpty()) {
            for (Token token : activeRefreshTokens) {
                if (token.getTokenType().equals(TokenType.REFRESH_TOKEN)) {
                    hasActiveRefreshToken = true;
                    break;
                }
            }
        }

        if (hasActiveRefreshToken) {
            //generate a new access token and save it
            var jwtToken = jwtService.generateToken(user); //generate a JWT Token for the user's session (If the user logs out the token will be marked as invalid, if the user authenticates again a new token will be created and the old one will be updated to be invalid)

            saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60));//save the token
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .build();
        } else {
            throw new InternalError("Invalid Refresh Token. Log in again.");
        }


    }


    /**
     * Revokes all valid tokens associated with the specified user.
     * This method sets both 'expired' and 'revoked' flags to true for all valid tokens of the user,
     * effectively invalidating these tokens. The changes are persisted to the database.
     *
     * @param user The user whose tokens are to be revoked.
     */
    private void revokeAllUserTokens(User user) {
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


    /**
     * Creates and saves a token for the given user in the database.
     * This method constructs a new token object with the specified attributes and saves it to the database.
     * It is used to generate both access and refresh tokens as part of the authentication and token refresh processes.
     *
     * @param savedUser The user for whom the token is being created.
     * @param jwtToken  The token string.
     * @param tokenType The type of token being created (e.g., ACCESS_TOKEN, REFRESH_TOKEN).
     * @param issuedAt  The date and time when the token was issued.
     * @param expiresAt The date and time when the token will expire.
     */
    private void saveUserToken(User savedUser, String jwtToken, TokenType tokenType, Date issuedAt, Date expiresAt) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(tokenType)
                .revoked(false)
                .expired(false)
                .ExpirationTime(expiresAt)
                .IssuedAt(issuedAt)
                .build();//here we create a Token object

        tokenRepository.save(token);//we save the token object in our database
    }


    /**
     * Retrieves the user associated with the current security context.
     * Useful for operations that require the currently authenticated user.
     *
     * @return The currently authenticated user.
     * @throws UsernameNotFoundException if the user cannot be found in the context.
     */
    public User getCurrentlyLoggedUser() { //throws the cringe
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // This returns the username/email of the authenticated user

        return userRepository.findByEmail(email)
                .orElseThrow();
    }


    /**
     * Retrieves a user based on a provided JWT token string.
     * This method is typically used to associate a JWT token with a user entity.
     *
     * @param jwtToken The JWT token associated with the user.
     * @return The user associated with the provided JWT token.
     * @throws UsernameNotFoundException if no user is found associated with the token.
     */
    public User getUserByJwtToken(String jwtToken) {
        System.out.println(userRepository.findByTokens_Token(jwtToken).orElseThrow());
        return userRepository.findByTokens_Token(jwtToken).orElseThrow();
    }

    /**
     * Provides the details of the currently authenticated user.
     * A convenience method that wraps the logic of {@link #getCurrentlyLoggedUser()}.
     *
     * @return The details of the currently authenticated user.
     */
    public User responseUserDetails() {


        return getCurrentlyLoggedUser();
    }
}