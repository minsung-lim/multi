package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.TokenResponse;
import com.auth.entity.AccessToken;
import com.auth.repository.AccessTokenRepository;
import com.auth.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final AccessTokenRepository accessTokenRepository;


    public TokenResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticating user: {}", loginRequest.getLoginId());
        
        // Call profile service to verify user
        ResponseEntity<String> profileResponse;// = restTemplate.getForEntity(profileUrl, String.class);
//
//        if (profileResponse.getStatusCode().is2xxSuccessful()) {
//            String token = jwtTokenUtil.generateToken(loginRequest.getLoginId());
//
//            // Save token to database
//            AccessToken accessToken = new AccessToken();
//            accessToken.setToken(token);
//            accessToken.setLoginId(loginRequest.getLoginId());
//            accessToken.setIssuedAt(LocalDateTime.now());
//            accessToken.setExpiresAt(LocalDateTime.now().plusHours(1));
//            accessToken.setRevoked(false);
//            accessTokenRepository.save(accessToken);
//
//            // Create OAuth2 compliant response
//            TokenResponse response = new TokenResponse();
//            response.setAccess_token(token);
//            response.setToken_type("Bearer");
//            response.setExpires_in(3600);
//            response.setScope("read write");
//
//            log.info("Successfully authenticated user: {}", loginRequest.getLoginId());
//            return response;
//        }

        log.error("Failed to authenticate user: {}", loginRequest.getLoginId());
        throw new RuntimeException("Authentication failed");
    }

    public void revokeToken(String token) {
        accessTokenRepository.findByToken(token)
                .ifPresent(accessToken -> {
                    accessToken.setRevoked(true);
                    accessTokenRepository.save(accessToken);
                    log.info("Token revoked for user: {}", accessToken.getLoginId());
                });
    }
} 