package com.example.userService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class for managing JSON Web Tokens (JWT).
 * <p>
 * This class is responsible for:
 * <ul>
 *     <li>Generating JWT tokens with HMAC-SHA256 signature.</li>
 *     <li>Extracting claims such as the user's email from tokens.</li>
 *     <li>Validating tokens (checking for expiration, integrity, format, etc.).</li>
 * </ul>
 * Requires the configuration properties {@code jwt.secret} and {@code jwt.expiration}.
 */
@Slf4j
@Component
public class JwtUtil {

    /** Base64-encoded secret key from application properties. */
    @Value("${jwt.secret}")
    private String secret;

    /** Token expiration time in milliseconds. */
    @Value("${jwt.expiration}")
    private long expiration;

    /** Decoded secret key used to sign and verify tokens. */
    private SecretKey secretKey;


    /**
     * Initializes the secret key after the component is constructed.
     * <p>This method is called automatically after property injection using {@code @PostConstruct}.</p>
     */
    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Generates a JWT token for a given user email.
     *
     * @param email the user's email to set as the subject of the token
     * @return a signed JWT token string with issued and expiration dates
     */
    public String generateToken(String email) {
        var now = Instant.now();
        var expiry = now.plusMillis(expiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (email) from a JWT token.
     *
     * @param token the JWT token
     * @return the subject (username/email) stored in the token
     * @throws JwtException if the token is invalid or parsing fails
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a given JWT token by checking its signature, structure, and expiration.
     *
     * @param token the JWT token to validate
     * @return {@code true} if the token is valid; {@code false} otherwise
     */
    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return claimsJws != null;
        } catch (ExpiredJwtException e) {
            log.info("Expired token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported Jwt  : " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.info("Malformed Jwt: " + e.getMessage());
        } catch (SecurityException | SignatureException e) {
            log.info("Invalid Sign: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("Illegal Argument: " + e.getMessage());
        }
        return false;
    }

}
