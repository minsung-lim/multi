package com.account.auth.service;

import com.account.auth.dto.LoginRequest;
import com.account.auth.dto.TokenRequest;
import com.account.auth.dto.TokenResponse;
import com.account.auth.model.Code;
import com.account.auth.model.Token;
import com.account.auth.repository.CodeRepository;
import com.account.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final CodeRepository codeRepository;
    private final TokenRepository tokenRepository;
    private final ProfileService profileService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MetadataService metadataService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${auth.token.scope:read write}")
    private String defaultScope;

    @Transactional
    public TokenResponse generateToken(TokenRequest request) {
        // clientId 유효성 검사
        if (request.getClientId() == null || request.getClientId().trim().isEmpty()) {
            throw new RuntimeException("Client ID is required");
        }

        // grant type 유효성 검사
        List<String> allowedGrantTypes = metadataService.getGrantTypes(request.getClientId());
        if (!allowedGrantTypes.contains(request.getGrantType())) {
            throw new RuntimeException("Invalid grant type");
        }

        // authorization code 검증
        Code code = codeRepository.findByCode(request.getCode())
                .orElseThrow(() -> new RuntimeException("Invalid code"));

        if (code.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Code has expired");
        }

        // 사용자 프로필 조회
        var profile = profileService.getProfile(code.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 기존 토큰 삭제
        tokenRepository.deleteByLoginIdAndClientId(profile.getLoginId(), request.getClientId());

        // 새 토큰 생성
        String token = generateRandomCode(30);
        String accessToken = jwtTokenProvider.generateAccessToken(profile);

        Token tokenVO = new Token();
        tokenVO.setToken(token);
        tokenVO.setLoginId(profile.getLoginId());
        tokenVO.setUserId(profile.getUserId());
        tokenVO.setClientId(request.getClientId());
        tokenVO.setAccessToken(accessToken);
        tokenVO.setRefreshToken(token);
        tokenVO.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        tokenVO.setScope(defaultScope);
        tokenRepository.save(tokenVO);

        // 사용된 코드 삭제
        codeRepository.delete(code);

        return new TokenResponse(accessToken, token, 1800L);
    }

    @Transactional
    public void revokeToken(String loginId, String clientId) {
        if (clientId == null || clientId.trim().isEmpty()) {
            tokenRepository.deleteByLoginId(loginId);
        } else {
            tokenRepository.deleteByLoginIdAndClientId(loginId, clientId);
        }
    }

    @Transactional
    public String generateCode(LoginRequest request) {
        // 사용자 인증
        var profile = profileService.authenticate(request.getLoginId(), request.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // 기존 코드 삭제
        codeRepository.deleteByUserId(profile.getUserId());

        // 새 코드 생성
        String code = generateRandomCode(20);
        Code codeEntity = new Code();
        codeEntity.setCode(code);
        codeEntity.setUserId(profile.getUserId());
        codeEntity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        codeRepository.save(codeEntity);

        return code;
    }

    @Transactional
    public int cleanExpiredCodes() {
        int totalDeleted = 0;
        LocalDateTime now = LocalDateTime.now();
        
        while (true) {
            List<Code> expiredCodes = codeRepository.findTop10ByExpiresAtBeforeOrderByExpiresAtAsc(now);
            if (expiredCodes.isEmpty()) {
                break;
            }
            
            codeRepository.deleteAll(expiredCodes);
            totalDeleted += expiredCodes.size();
        }
        
        return totalDeleted;
    }

    private String generateRandomCode(int size) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            code.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return code.toString();
    }
} 