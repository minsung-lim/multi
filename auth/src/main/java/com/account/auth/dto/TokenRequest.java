package com.account.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Token request")
public class TokenRequest {
    @Schema(description = "Authorization code", example = "abc123xyz")
    private String code;

    @Schema(description = "Client ID", example = "client123")
    private String client_id;

    @Schema(description = "Grant type", example = "authorization_code")
    private String grant_type = "authorization_code";
} 