package com.account.metadata.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ApplicationResponse {
    private String appId;
    private String appName;
    private Set<String> scopes;
    private String secretKey;
    private String cipherKey;
    private String redirectUri;
    private Set<String> grantTypes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
} 