package com.account.auth.service;

import com.account.auth.dto.LoginRequest;
import com.account.auth.dto.TokenRequest;
import com.account.auth.dto.TokenResponse;
import com.account.auth.model.AccessToken;
import com.account.auth.model.Code;
import com.account.auth.repository.AccessTokenRepository;
import com.account.auth.repository.CodeRepository;
import com.account.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final CodeRepository codeRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${auth.issuer:https://auth.example.com}")
    private String issuer;

    @Value("${auth.audience:https://api.example.com}")
    private String audience;

//    @Transactional
    public TokenResponse generateToken(TokenRequest request) {
        // Validate code
        Code code = codeRepository.findById(request.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid code"));

        // Delete used code
        codeRepository.delete(code);

        // Check if code is expired (5 minutes)
        if (code.getCreateDatetime().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code expired");
        }

        // Get user from code
        var user = userRepository.findById(code.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user"));

        // Generate access token
        String token = generateAlphanumericToken();
        String scope = "read write"; // You might want to make this configurable

        // Save access token
        AccessToken accessToken = new AccessToken(token, user.getUserId(), user.getLoginId(), request.getClient_id(), scope);
        accessTokenRepository.save(accessToken);

        // Create JWT response
        Instant now = Instant.now();
        return new TokenResponse(
            token,
            "Bearer",
            3600,
            scope,
            issuer,
            audience,
            user.getUserId(),
            now.getEpochSecond(),
            now.plusSeconds(3600).getEpochSecond()
        );
    }

    @Transactional
    public void revokeToken(String token) {
        AccessToken accessToken = accessTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token"));
        accessTokenRepository.save(accessToken);
    }

    @Transactional
    public String generateCode(LoginRequest loginRequest) {
        var user = userRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request"));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        String code = generateAlphanumericCode();
        Code codeEntity = new Code(code);
        codeEntity.setUserId(user.getUserId());
        codeRepository.save(codeEntity);
        return code;
    }

    private String generateAlphanumericToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            token.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return token.toString();
    }

    private String generateAlphanumericCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder(30);
        for (int i = 0; i < 30; i++) {
            code.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return code.toString();
    }
} 