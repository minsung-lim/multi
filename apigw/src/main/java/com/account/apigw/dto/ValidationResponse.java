package com.account.apigw.dto;

import lombok.Data;

@Data
public class ValidationResponse {
    private boolean valid;
    private String message;
} 