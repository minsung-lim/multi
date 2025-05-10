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

    @Schema(description = "Token type", example = "Bearer")
    private String token_type = "Bearer";

    @Schema(description = "Token expiration in seconds", example = "3600")
    private long expires_in = 3600;

    @Schema(description = "Token scope", example = "read write")
    private String scope;

    @Schema(description = "JWT issuer", example = "https://auth.example.com")
    private String iss;

    @Schema(description = "JWT audience", example = "https://api.example.com")
    private String aud;

    @Schema(description = "JWT subject", example = "user123")
    private String sub;

    @Schema(description = "JWT issued at timestamp", example = "1516239022")
    private long iat;

    @Schema(description = "JWT expiration timestamp", example = "1516242622")
    private long exp;

    public TokenResponse(String access_token) {
        this.access_token = access_token;
    }
} 