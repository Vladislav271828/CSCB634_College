package com.mvi.CSCB634College.security.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Provides services for managing JWT tokens in a Spring Security context.
 * This service allows the generation, validation, and extraction of information from JWT tokens
 * using the HS256 signature algorithm. It is designed to be used as a part of Spring Security
 * authentication processes.
 */
@Service
public class JwtService {

    // Secret key for signing JWT tokens. This must be kept secure.
    private static final String SECRET_KEY = "d1a09b487cb1e2119a2c9cdd3612d3026128694ac868dc2c07e96ddd1c7183c6";


    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a new JWT token for the given user details.
     *
     * @param userDetails the user details to generate a token for
     * @return a new JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a new JWT token with additional claims for the given user details.
     *
     * @param extraClaims additional claims to include in the token
     * @param userDetails the user details to generate a token for
     * @return a new JWT token
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a new JWT refresh token for the given user details with a longer expiration.
     *
     * @param userDetails the user details to generate a refresh token for
     * @return a new JWT refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        // Set long expiration time for refresh tokens
        long expirationTimeLong = 1000 * 60 * 60 * 24; // 1 day
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeLong))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if a token is valid for the given user details.
     *
     * @param token       the token to check
     * @param userDetails the user details to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Helper method to check if a token is expired.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Helper method to extract the expiration date from a token.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a claim from the given token using the provided claims resolver function.
     *
     * @param token          the token to extract the claim from
     * @param claimsResolver the function to apply to the claims for extracting the desired information
     * @param <T>            the type of the extracted claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Helper method to extract all claims from a token.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Helper method to get the signing key from the base64 encoded secret key.
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}