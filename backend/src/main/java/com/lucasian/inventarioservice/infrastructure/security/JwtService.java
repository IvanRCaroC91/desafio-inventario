package com.lucasian.inventarioservice.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    private final JwtProperties properties;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
    }

    public String generateToken(String subject) {
        SecretKey key = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));

        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(60L * 60L)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(properties.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
