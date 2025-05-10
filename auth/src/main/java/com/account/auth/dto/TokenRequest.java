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
@Schema(description = "Token request")
public class TokenRequest {
    @Schema(description = "Authorization code", example = "abc123xyz")
    private String code;

    @Schema(description = "Client ID", example = "client123")
    private String clientId;

    @Schema(description = "Grant type", example = "authorization_code")
    private String grantType;
} 