package com.account.auth.service;

import com.account.auth.dto.Profile;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${auth.issuer:https://auth.example.com}")
    private String issuer;

    @Value("${auth.jwt.secret:your-256-bit-secret-your-256-bit-secret}")
    private String jwtSecret;

    public String generateAccessToken(Profile profile) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiresAt = Date.from(now.plusSeconds(1800));

        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", "read write");
        claims.put("login_id", profile.getLoginId());

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(profile.getUserId())
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Profile profile) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiresAt = Date.from(now.plusSeconds(604800)); // 7 days

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("login_id", profile.getLoginId());

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(profile.getUserId())
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
} 