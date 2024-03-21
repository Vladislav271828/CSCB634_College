package com.mvi.CSCB634College.security.auth;



import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.config.JwtService;
import com.mvi.CSCB634College.security.token.Token;
import com.mvi.CSCB634College.security.token.TokenRepository;
import com.mvi.CSCB634College.security.token.TokenType;
import com.mvi.CSCB634College.user.User;
import com.mvi.CSCB634College.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;


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

        saveUserToken(savedUser, jwtToken);//save the token

        return AuthenticationResponse.builder()
                .token(jwtToken)//we return a jwt token that can be used by the client to authenticate other requests
                .build()
                ;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {


        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();//we do this and need it to there after revoke all previous jwt tokens


        authenticationManager.authenticate(//authenticates the user
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        revokeAllUserTokens(user);//ensures jwt tokens are set to false before creating a new one to avoid addition of more than one valid jwt tokens

        var jwtToken = jwtService.generateToken(user);//generates a new token
        saveUserToken(user, jwtToken);//saves user token - saves session in a way

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build()
                ;
    }


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


    private void saveUserToken(User savedUser, String jwtToken) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();//here we create a Token object

        tokenRepository.save(token);//we save the token object in our database
    }


    //ultra cool method, I use it every time (Not sure if this is good practice ¯\_( ͡° ͜ʖ ͡°)_/¯)
    public User getCurrentlyLoggedUser() { //throws the cringe
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // This returns the username/email of the authenticated user

        return userRepository.findByEmail(email)
                .orElseThrow();
    }


    public User getUserByJwtToken(String jwtToken) {
        System.out.println(userRepository.findByTokens_Token(jwtToken).orElseThrow());
        return userRepository.findByTokens_Token(jwtToken).orElseThrow();
    }
}