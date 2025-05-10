package com.account.metadata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ApplicationRequest {
    // @NotBlank(message = "App ID is required")
    // private String appId;

    @NotBlank(message = "App name is required")
    private String appName;

    @NotEmpty(message = "At least one scope is required")
    private Set<String> scopes;

    @NotBlank(message = "Secret key is required")
    private String secretKey;

    @NotBlank(message = "Cipher key is required")
    private String cipherKey;

    private String redirectUri;

    @NotEmpty(message = "At least one grant type is required")
    private Set<String> grantTypes;
} 