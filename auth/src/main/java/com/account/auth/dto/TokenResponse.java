package com.account.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Token response")
public class TokenResponse {
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String access_token;

    private String refresh_token;

    @Schema(description = "Token expiration in seconds", example = "3600")
    private long expires_in = 3600;

    public TokenResponse(String access_token) {
        this.access_token = access_token;
    }
} 