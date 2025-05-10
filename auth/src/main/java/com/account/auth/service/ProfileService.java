package com.account.auth.service;

import com.account.auth.dto.LoginRequest;
import com.account.auth.dto.Profile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ObjectMapper objectMapper;

    @Value("${profile.service.url:http://localhost:8084}")
    private String profileServiceUrl;

    public Optional<Profile> getProfile(String loginId) {
        if (loginId == null || loginId.trim().isEmpty()) {
            log.warn("Attempted to get profile with null or empty loginId");
            return Optional.empty();
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(profileServiceUrl + "/profile/user/" + loginId);
            return httpClient.execute(request, response -> {
                if (response.getCode() != 200) {
                    log.warn("Failed to get profile for loginId: {}, status code: {}", loginId, response.getCode());
                    return Optional.empty();
                }

                JsonNode root = objectMapper.readTree(response.getEntity().getContent());
                if (!isValidProfileResponse(root)) {
                    log.warn("Invalid profile response for loginId: {}", loginId);
                    return Optional.empty();
                }

                Profile profile = new Profile();
                profile.setUserId(getTextSafely(root, "userId"));
                profile.setLoginId(getTextSafely(root, "loginId"));
                profile.setName(getTextSafely(root, "name"));
                profile.setEmail(getTextSafely(root, "email"));
                return Optional.of(profile);
            });
        } catch (IOException e) {
            log.error("Error fetching profile for loginId: " + loginId, e);
            throw new RuntimeException("Failed to fetch profile: " + e.getMessage());
        }
    }

    public Optional<Profile> authenticate(String loginId, String password) {
        if (loginId == null || loginId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            log.warn("Attempted to authenticate with null or empty credentials");
            return Optional.empty();
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(profileServiceUrl + "/profile/user/authenticate");
            
            String requestBody = objectMapper.writeValueAsString(new LoginRequest(loginId, password));
            request.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));

            return httpClient.execute(request, response -> {
                if (response.getCode() != 200) {
                    log.warn("Authentication failed for loginId: {}, status code: {}", loginId, response.getCode());
                    return Optional.empty();
                }

                JsonNode root = objectMapper.readTree(response.getEntity().getContent());
                if (!isValidProfileResponse(root)) {
                    log.warn("Invalid authentication response for loginId: {}", loginId);
                    return Optional.empty();
                }

                Profile profile = new Profile();
                profile.setUserId(getTextSafely(root, "userId"));
                profile.setLoginId(getTextSafely(root, "loginId"));
                profile.setName(getTextSafely(root, "name"));
                profile.setEmail(getTextSafely(root, "email"));
                return Optional.of(profile);
            });
        } catch (IOException e) {
            log.error("Error authenticating user: " + loginId, e);
            throw new RuntimeException("Failed to authenticate: " + e.getMessage());
        }
    }

    private boolean isValidProfileResponse(JsonNode root) {
        if (root == null) {
            return false;
        }

        // 필수 필드 존재 여부 확인
        if (!root.has("userId") || !root.has("loginId")) {
            return false;
        }

        // 필수 필드가 null이 아닌지 확인
        JsonNode userIdNode = root.get("userId");
        JsonNode loginIdNode = root.get("loginId");
        
        return !userIdNode.isNull() && !loginIdNode.isNull() &&
               !userIdNode.asText().trim().isEmpty() && 
               !loginIdNode.asText().trim().isEmpty();
    }

    private String getTextSafely(JsonNode node, String fieldName) {
        if (node == null || !node.has(fieldName)) {
            return null;
        }
        JsonNode fieldNode = node.get(fieldName);
        return fieldNode != null && !fieldNode.isNull() ? fieldNode.asText().trim() : null;
    }
} 